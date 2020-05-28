package edu.rice.cs.repositories;

import edu.rice.cs.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by songxiongfeng on 5/22/20
 */
public interface ReviewRepository extends CrudRepository<Review, String> {

    Review findByUserIdAndProductId(String userId, String productId);

}
