package edu.rice.cs.repositories;

import edu.rice.cs.model.UserRecList;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RecommendationRepository extends MongoRepository<UserRecList, String> {
    Optional<UserRecList> findByUserId(String userId);
}
