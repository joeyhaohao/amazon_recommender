package edu.rice.cs.controller;

import edu.rice.cs.exception.ProductNotFoundException;
import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.RecommendItem;
import edu.rice.cs.model.RecommendList;
import edu.rice.cs.model.User;
import edu.rice.cs.repositories.ProductRepository;
import edu.rice.cs.repositories.RecommendationRepository;
import edu.rice.cs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songxiongfeng on 5/20/20
 */
@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping(value = "/")
    public String getIndex(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        RecommendList cfRecs = recommendationRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RecommendationNotFoundException(username));
        List<Product> cfProdList = new ArrayList<>();
        for (RecommendItem item: cfRecs.getRecList()) {
            Product product = productRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));
            cfProdList.add(product);
        }

//        String REALTIME_REC_COLLECTION = "realtime_recommendation";
//        RecommendList realtimeRecs = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(user.getUserId())), RecommendList.class, REALTIME_REC_COLLECTION);
//        List<Product> realtimeProdList = new ArrayList<>();
//        for (RecommendItem item: realtimeRecs.getRecList()) {
//            Product product = productRepository.findByProductId(item.getProductId())
//                    .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));
//            realtimeProdList.add(product);
//        }
        model.addAttribute("username", username);
        model.addAttribute("cfRecs", cfProdList);
//        model.addAttribute("realtimeRecs", realtimeProdList);

        return "index";
    }

//
}
