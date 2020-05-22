package edu.rice.cs.controller;

import edu.rice.cs.model.RecommendItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import edu.rice.cs.service.RecommenderService;

import java.util.List;

@Controller
public class ProductController {

    @GetMapping("/product/itemcf/{id}")
    public String getItemCF(@PathVariable("id") String productID, Model model) {
        List<RecommendItem> recList = RecommenderService.getProductItemCF(productID);
        model.addAttribute("success", true);
//        model.addAttribute("products", productService.getRecommendProducts(recommendations));
        return "itemcf";
    }

}
