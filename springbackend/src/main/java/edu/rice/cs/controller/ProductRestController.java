package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.Rating;
import edu.rice.cs.payload.ApiResponse;
import edu.rice.cs.payload.ProductResponse;
import edu.rice.cs.payload.SearchRequest;
import edu.rice.cs.repositories.ProductRepository;
import edu.rice.cs.repositories.RatingRepository;
import edu.rice.cs.service.KafkaProducer;
import edu.rice.cs.service.SearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by songxiongfeng on 5/22/20
 */

@RestController
@RequestMapping("/product")
public class ProductRestController {

    private double SEARCH_SCORE = 4.0;
    private static Logger logger = LogManager.getLogger(ProductRestController.class.getName());

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SearchService searchService;

    @PostMapping("/")
    Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/{productId}")
    ProductResponse getProduct(@PathVariable String productId) {
        String PRODUCT_COLLECTION = "product";
        String RATING_COLLECTION = "rating";
//        // Get product from MongoDB
//        Product product = mongoTemplate.findOne(Query.query(Criteria.where("productId").is(productId)), Product.class, PRODUCT_COLLECTION);
//        if (product == null) {
//            throw new ProductNotFoundException(productId);
//        }
        // Get product from Elasticsearch
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        List<Rating> ratingList = mongoTemplate.find(
                Query.query(Criteria.where("productId").is(productId)), Rating.class, RATING_COLLECTION);
        return new ProductResponse(product, ratingList);
    }

    @PutMapping("/{productId}")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable String productId) {
        return productRepository.findByProductId(productId)
                .map(product -> {
                            product.setProductId(newProduct.getProductId());
                            product.setTitle(newProduct.getTitle());
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

    @PostMapping(value = "/rate/{productId}", produces = "application/json")
    ApiResponse rateProduct(@PathVariable("productId") String productId, @RequestParam("userId") String userId, @RequestParam("rate") Double score) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        String key = "userId:" + userId;
        String value = "rating|" + productId + "|" + score;
        ops.leftPush(key, value);
        logger.info(String.format("Save rating event to Redis, key: %s, value: %s", key, value));

        Rating rating = ratingRepository.findByUserIdAndProductId(userId, productId);
        // update the rating if exists
        if (rating != null) {
            rating.setRating(score);
            rating.setTimestamp(System.currentTimeMillis() / 1000);
        } else {
            rating = new Rating(userId, productId, score, System.currentTimeMillis() / 1000);
        }
        ratingRepository.save(rating);
        logger.info(String.format("Save rating to mongoDB: %s", rating.toString()));

        String msg = "rating|" + userId + "|" + productId + "|" + score + "|" + System.currentTimeMillis() / 1000;
        logger.info(String.format("Send message to Kafka: %s", msg));
        kafkaProducer.sendMessage(msg);

        return new ApiResponse(true, "rate success");
    }

    @PostMapping("/search")
    ResponseEntity<List<Product>> search(@RequestBody SearchRequest request) {
        List<Product> searchResult = searchService.search(request);
        String userId = request.getUserId();
        String productId = searchResult.get(0).getProductId();

        ListOperations<String, String> ops = redisTemplate.opsForList();
        String key = "userId:" + userId;
        String value = "search|" + productId + "|" + SEARCH_SCORE;
        ops.leftPush(key, value);
        logger.info(String.format("Save search event to Redis, key: %s, value: %s", key, value));

        String msg = "search|" + userId + "|" + productId + "|" + SEARCH_SCORE + "|" + System.currentTimeMillis() / 1000;
        logger.info(String.format("Send message to Kafka: %s", msg));
        kafkaProducer.sendMessage(msg);
        return ResponseEntity.ok(searchResult);
    }
}
