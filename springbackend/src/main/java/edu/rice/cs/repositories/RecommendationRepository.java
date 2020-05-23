package edu.rice.cs.repositories;

import edu.rice.cs.model.RecommendList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface RecommendationRepository extends MongoRepository<RecommendList, String> {
}
