package edu.rice.cs.controller;

import edu.rice.cs.exception.UserNotFoundException;
import edu.rice.cs.model.Product;
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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
//                .orElseThrow(() -> new UserNotFoundException(username));
//        return userRepository.findAll();
    }

    @PutMapping(value = "/{username}", produces = "application/json")
    User updateUser(@PathVariable String username, @RequestBody User newUser) {
        User user = userRepository.findByUsername(username);
        user.setUserId(newUser.getUserId());
        user.setUsername(newUser.getUsername());
        return user;
    }

    @PostMapping("/")
    User newUser(@RequestBody User newUser) {
        User user = userRepository.findByUsername(newUser.getUsername());
        if (user != null) {
            throw new IllegalArgumentException("User already exists.");
        }
        userRepository.save(newUser);
        return newUser;
    }

}
