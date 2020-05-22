package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.Review;
import edu.rice.cs.repositories.ProductRepository;
import edu.rice.cs.repositories.ReviewRepository;
import edu.rice.cs.repositories.UserRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by songxiongfeng on 5/22/20
 */
@RestController
public class RestfulController {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public RestfulController(ProductRepository productRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/products")
    List<Product> getProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/products")
    Product newProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/products/{id}")
    Product getOneProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/products/{id}")
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

    @DeleteMapping("products/{id}")
    void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
