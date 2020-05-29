package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.Review;
import edu.rice.cs.repositories.ProductRepository;
import edu.rice.cs.repositories.ReviewRepository;
import edu.rice.cs.repositories.UserRepository;
import edu.rice.cs.service.KafkaProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

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

    @GetMapping("/{id}")
    Product getOneProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/{id}")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                            product.setProductID(newProduct.getProductID());
                            product.setProductName(newProduct.getProductName());
                            product.setImageUrl(newProduct.getImageUrl());
                            product.setCategory(newProduct.getCategory());
                            return productRepository.save(product);
                        }
                ).orElseGet(() -> {
                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @PostMapping("/rate/{productId}")
    void rateProduct(@PathVariable("productId") String productId, @RequestParam("userId") String userId, @RequestParam("rate") Double rate) {
        Review review = reviewRepository.findByUserIdAndProductId(userId, productId);
        // update the rating if exists
        if (review != null) {
            review.setRate(rate);
            review.setTimestamp(System.currentTimeMillis() / 1000);
        } else {
            review = new Review(userId, productId, rate, System.currentTimeMillis() / 1000);
        }
        reviewRepository.save(review);
        logger.info(String.format("Save review to Redis: %s", review.toString()));

        String msg = userId + "|" + productId + "|" + rate + "|" + System.currentTimeMillis() / 1000;
        kafkaProducer.sendMessage(msg);
    }
}
