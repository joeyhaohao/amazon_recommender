package edu.rice.cs.controller;

import edu.rice.cs.model.RecommendItem;
import net.minidev.json.JSONObject;
import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.RecommendList;
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

    @GetMapping(value = "/cf/{userId}", produces = "application/json")
    RecommendList getCFRecommendation(@PathVariable String userId) {
        return recommendationRepository.findByUserId(userId)
                .orElseThrow(() -> new RecommendationNotFoundException(userId));
//        return recommendationRepository.findAll();
    }

    @GetMapping(value = "/realtime/{userId}", produces = "application/json")
    RecommendList getRealtimeRecommendation(@PathVariable String userId) {
        String REALTIME_REC_COLLECTION = "realtime_recommendation";
        RecommendList recList = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), RecommendList.class, REALTIME_REC_COLLECTION);
        return recList;
    }
}
