package edu.rice.cs.bootstrap;

import edu.rice.cs.model.Employee;
import edu.rice.cs.model.Manager;
import edu.rice.cs.model.User;
import edu.rice.cs.repositories.EmployeeRepository;
import edu.rice.cs.repositories.ManagerRepository;
import edu.rice.cs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by songxiongfeng on 5/20/20
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final EmployeeRepository employees;
    private final ManagerRepository managers;
    private final UserRepository users;

    @Autowired
    public DatabaseLoader(EmployeeRepository employeeRepository,
                          ManagerRepository managerRepository, UserRepository userRepository) {

        this.employees = employeeRepository;
        this.managers = managerRepository;
        this.users = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("+++++++++++++");

        User sxf = this.users.save(new User("123", "sxf", "12345"));
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken("sxf", "doesn't matter",
//                        AuthorityUtils.createAuthorityList("user")));

        System.out.println("+++++++++++++");

        Manager greg = this.managers.save(new Manager("greg", "turnquist",
                "ROLE_MANAGER"));
//        Manager oliver = this.managers.save(new Manager("oliver", "gierke",
//                "ROLE_MANAGER"));
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken("greg", "doesn't matter",
//                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));
//
//        this.employees.save(new Employee("Frodo", "Baggins", "ring bearer", greg));
//        this.employees.save(new Employee("Bilbo", "Baggins", "burglar", greg));
//        this.employees.save(new Employee("Gandalf", "the Grey", "wizard", greg));
//
//        SecurityContextHolder.getContext().setAuthentication(
//                new UsernamePasswordAuthenticationToken("oliver", "doesn't matter",
//                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));
//
//        this.employees.save(new Employee("Samwise", "Gamgee", "gardener", oliver));
//        this.employees.save(new Employee("Merry", "Brandybuck", "pony rider", oliver));
//        this.employees.save(new Employee("Peregrin", "Took", "pipe smoker", oliver));
//
//        SecurityContextHolder.clearContext();
    }
}
