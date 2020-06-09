package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.Review;
import edu.rice.cs.repositories.ProductRepository;
import edu.rice.cs.repositories.ReviewRepository;
import edu.rice.cs.service.KafkaProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    private KafkaProducer kafkaProducer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
                            product.setProductName(newProduct.getProductName());
                            product.setImageUrl(newProduct.getImageUrl());
                            product.setCategory(newProduct.getCategory());
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

    @PostMapping("/rate/{productId}")
    void rateProduct(@PathVariable("productId") String productId, @RequestParam("userId") String userId, @RequestParam("rate") Double rate) {
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
    }
}
