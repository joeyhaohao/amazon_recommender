package edu.rice.cs.controller;

import edu.rice.cs.model.ProductRecList;
import edu.rice.cs.model.RecommendItem;
import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.UserRecList;
import edu.rice.cs.repositories.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommender")
public class RecommenderRestController {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @GetMapping(value = "/trending", produces = "application/json")
    List<RecommendItem> getTrendingRecommendation() {
        String TRENDING_REC_COLLECTION = "trending_recommendation";
        List<RecommendItem> recList = mongoTemplate.findAll(RecommendItem.class, TRENDING_REC_COLLECTION);
        return recList;
    }

    @GetMapping(value = "/top_rate", produces = "application/json")
    List<RecommendItem> getTopRateRecommendation() {
        String TOPRATE_REC_COLLECTION = "top_rate_recommendation";
        List<RecommendItem> recList = mongoTemplate.findAll(RecommendItem.class, TOPRATE_REC_COLLECTION);
        return recList;
    }

    @GetMapping(value = "/als/{userId}", produces = "application/json")
    UserRecList getCFRecommendation(@PathVariable String userId) {
        return recommendationRepository.findByUserId(userId)
                .orElseThrow(() -> new RecommendationNotFoundException(userId));
//        return recommendationRepository.findAll();
    }

    @GetMapping(value = "/realtime/{userId}", produces = "application/json")
    UserRecList getRealtimeRecommendation(@PathVariable String userId) {
        String REALTIME_REC_COLLECTION = "realtime_recommendation";
        UserRecList recList = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), UserRecList.class, REALTIME_REC_COLLECTION);
        return recList;
    }

    @GetMapping(value = "/itemcf/{productId}", produces = "application/json")
    ProductRecList getItemCFRecommendation(@PathVariable String productId) {
        String ITEMCF_REC_COLLECTION = "itemcf_recommendation";
        ProductRecList recList = mongoTemplate.findOne(
                Query.query(Criteria.where("productId").is(productId)), ProductRecList.class, ITEMCF_REC_COLLECTION);
        return recList;
    }
}
