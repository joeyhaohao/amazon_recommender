package edu.rice.cs.repositories;

import edu.rice.cs.model.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by songxiongfeng on 5/21/20
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
}
