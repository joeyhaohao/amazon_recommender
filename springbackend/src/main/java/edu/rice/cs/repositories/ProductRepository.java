package edu.rice.cs.repositories;

import edu.rice.cs.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Created by songxiongfeng on 5/21/20
 */
public interface ProductRepository extends MongoRepository<Product, Long> {
//    Product findByProductID(String productId);

    Optional<Product> findByProductId(String productId);

    void deleteByProductId(String productId);
}
