package edu.rice.cs.repositories;

import edu.rice.cs.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ProductRepository extends ElasticsearchRepository<Product, Long> {

    Optional<Product> findByProductId(String productId);

    void deleteByProductId(String productId);

}
