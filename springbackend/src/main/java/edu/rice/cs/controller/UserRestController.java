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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        return user;
    }

    @PutMapping(value = "/{username}", produces = "application/json")
    User updateUser(@PathVariable String username, @RequestBody User newUser) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUserId(newUser.getUserId());
        user.setUsername(newUser.getUsername());
        // ??
        return user;

    }

    @PostMapping("/")
    User newUser(@RequestBody User newUser) {
        User user = userRepository.findByUsername(newUser.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.save(newUser);
        return newUser;
    }

}
