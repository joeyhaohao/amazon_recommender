package edu.rice.cs.controller;

import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.exception.UserNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.RecommendResult;
import edu.rice.cs.model.User;
import edu.rice.cs.repositories.RecommendationRepository;
import edu.rice.cs.repositories.UserRepository;
import edu.rice.cs.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
