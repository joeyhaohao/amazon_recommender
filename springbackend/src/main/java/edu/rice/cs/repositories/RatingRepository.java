package edu.rice.cs.repositories;

import edu.rice.cs.model.Rating;
import edu.rice.cs.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {

    Rating findByUserIdAndProductId(String userId, String productId);

    List<Rating> findAllByUserId(String userId);

    List<Rating> findAllByProductId(String productId);

}
