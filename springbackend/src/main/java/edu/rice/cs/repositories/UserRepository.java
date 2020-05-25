package edu.rice.cs.repositories;

import edu.rice.cs.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Created by songxiongfeng on 5/22/20
 */
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
