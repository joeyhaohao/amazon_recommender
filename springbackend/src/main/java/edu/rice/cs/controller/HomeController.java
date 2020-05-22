package edu.rice.cs.controller;

import edu.rice.cs.repositories.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by songxiongfeng on 5/20/20
 */
@Controller
public class HomeController {
    private final ProductRepository productRepository;

    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping("/products")
    public String getProduct(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "products";
    }
}
