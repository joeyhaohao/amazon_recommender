package edu.rice.cs.controller;

import edu.rice.cs.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import edu.rice.cs.service.RecommenderService;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private RecommenderService recommenderService;

    @GetMapping(value = "/user/cf", produces = "application/json")
    @ResponseBody
    public Model getProductRecForUser(@RequestParam("id") String userID, @RequestParam("num") int num, Model model) {
        List<Product> recommendations = null;
        try {
            recommendations = recommenderService.getUserProductRec(userID, num);
            model.addAttribute("success", true);
            model.addAttribute("products", recommendations);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("msg", e.getMessage());
        }
        return model;
    }

}
