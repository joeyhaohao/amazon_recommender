package edu.rice.cs.repositories;

import edu.rice.cs.model.Review;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by songxiongfeng on 5/22/20
 */
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
