package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.RecommendResult;
import edu.rice.cs.repositories.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @GetMapping(value = "/cf/{userId}", produces = "application/json")
//    @ResponseBody
//    public Model getProductRecForUser(@RequestParam("id") String userID, @RequestParam("num") int num, Model model) {
//        List<Product> recommendations = null;
//        try {
//            recommendations = recommenderService.getUserProductRec(userID, num);
//            model.addAttribute("success", true);
//            model.addAttribute("products", recommendations);
//        } catch (Exception e) {
//            model.addAttribute("success", false);
//            model.addAttribute("msg", e.getMessage());
//        }
//        return model;
//    }
    RecommendResult getRecommendationForUser(@PathVariable String userId) {
        return recommendationRepository.findByUserId(userId)
                .orElseThrow(() -> new RecommendationNotFoundException(userId));
//        return recommendationRepository.findAll();
    }

}
