package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.RecommendList;
import edu.rice.cs.model.Review;
import edu.rice.cs.model.User;
import edu.rice.cs.repositories.ProductRepository;
import edu.rice.cs.repositories.RecommendationRepository;
import edu.rice.cs.repositories.ReviewRepository;
import edu.rice.cs.repositories.UserRepository;
import edu.rice.cs.service.KafkaProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by songxiongfeng on 5/22/20
 */

@RestController
@RequestMapping("/product")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static Logger logger = LogManager.getLogger(ProductRestController.class.getName());

    @GetMapping("/")
    List<Product> getProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/")
    Product newProduct(@RequestBody Product product) {
        System.out.println("product");
//        return productRepository.save(product);
        return null;
    }

    @GetMapping("/{productId}")
    Product getOneProduct(@PathVariable String productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @PutMapping("/{productId}")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable String productId) {
        return productRepository.findByProductId(productId)
                .map(product -> {
                            product.setProductId(newProduct.getProductId());
                            product.setImUrl(newProduct.getImUrl());
                            product.setCategories(newProduct.getCategories());
                            return productRepository.save(product);
                        }
                ).orElseGet(() -> {
                    newProduct.setProductId(productId);
                    return productRepository.save(newProduct);
                });
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable String productId) {
        productRepository.deleteByProductId(productId);
    }

    @PostMapping("/rate")
    RecommendList rateProduct(@RequestParam("productId") String productId, @RequestParam("rate") Double rate) {
        System.out.println("rate");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userRepository.findByUsername(username);
        String userId = user.getUserId();

        ListOperations<String, String> ops = redisTemplate.opsForList();
        String key = "userId:" + userId;
        String value = productId + ":" + rate;
        ops.leftPush(key, value);
        logger.info(String.format("Save rating to Redis, key: %s, value: %s", key, value));

        Review review = reviewRepository.findByUserIdAndProductId(userId, productId);
        // update the rating if exists
        if (review != null) {
            review.setRate(rate);
            review.setTimestamp(System.currentTimeMillis() / 1000);
        } else {
            review = new Review(userId, productId, rate, System.currentTimeMillis() / 1000);
        }
        reviewRepository.save(review);
        logger.info(String.format("Save review to mongoDB: %s", review.toString()));

        String msg = userId + "|" + productId + "|" + rate + "|" + System.currentTimeMillis() / 1000;
        logger.info(String.format("Send message to Kafka: %s", msg));
        kafkaProducer.sendMessage(msg);

        String REALTIME_REC_COLLECTION = "realtime_recommendation";
        RecommendList recList = mongoTemplate.findOne(
                Query.query(Criteria.where("userId").is(userId)), RecommendList.class, REALTIME_REC_COLLECTION);
        return recList;
    }
}
