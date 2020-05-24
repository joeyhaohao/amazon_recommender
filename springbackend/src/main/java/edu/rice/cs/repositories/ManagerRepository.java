package edu.rice.cs.repositories;

import edu.rice.cs.model.Manager;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by songxiongfeng on 5/23/20
 */
@RepositoryRestResource(exported = false)
public interface ManagerRepository extends Repository<Manager, Long> {
    Manager save(Manager manager);

    Manager findByName(String name);
}
