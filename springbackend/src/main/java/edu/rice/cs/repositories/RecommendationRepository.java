package edu.rice.cs.repositories;

import edu.rice.cs.model.RecommendResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RecommendationRepository extends MongoRepository<RecommendResult, String> {
    Optional<RecommendResult> findByUserId(String userId);
}
