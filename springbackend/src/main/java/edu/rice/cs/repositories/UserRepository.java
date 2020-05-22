package edu.rice.cs.repositories;

import edu.rice.cs.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by songxiongfeng on 5/22/20
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
