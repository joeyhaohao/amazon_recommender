package edu.rice.cs.controller;

import edu.rice.cs.exception.UserNotFoundException;
import edu.rice.cs.model.User;
import edu.rice.cs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/{username}", produces = "application/json")
    User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
//        return userRepository.findAll();
    }

}
