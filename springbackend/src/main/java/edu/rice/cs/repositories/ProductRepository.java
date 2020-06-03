package edu.rice.cs.repositories;

import edu.rice.cs.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by songxiongfeng on 5/21/20
 */
public interface ProductRepository extends MongoRepository<Product, Long> {
    Product findByProductID(String productId);
}
