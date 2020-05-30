package edu.rice.cs.repositories;

import edu.rice.cs.model.RecommendList;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RecommendationRepository extends MongoRepository<RecommendList, String> {
    Optional<RecommendList> findByUserId(String userId);
}
