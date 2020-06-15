package edu.rice.cs.controller;

import edu.rice.cs.model.ProductRecList;
import edu.rice.cs.model.RecommendItem;
import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.UserRecList;
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
    private MongoTemplate mongoTemplate;

    @GetMapping(value = "/trending", produces = "application/json")
    List<RecommendItem> getTrendingRecommendation() {
        String TRENDING_REC_COLLECTION = "trending_recommendation";
        List<RecommendItem> recList = mongoTemplate.findAll(RecommendItem.class, TRENDING_REC_COLLECTION);
        if (recList == null) {
            throw new RecommendationNotFoundException("trending", "");
        }
        return recList;
    }

    @GetMapping(value = "/top_rate", produces = "application/json")
    List<RecommendItem> getTopRateRecommendation() {
        String TOPRATE_REC_COLLECTION = "top_rate_recommendation";
        List<RecommendItem> recList = mongoTemplate.findAll(RecommendItem.class, TOPRATE_REC_COLLECTION);
        if (recList == null) {
            throw new RecommendationNotFoundException("top rate", "");
        }
        return recList;
    }

    @GetMapping(value = "/als/{userId}", produces = "application/json")
    UserRecList getALSRecommendation(@PathVariable String userId) {
        String ALS_REC_COLLECTION = "als_recommendation";
        UserRecList recList = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), UserRecList.class, ALS_REC_COLLECTION);
        if (recList == null) {
            throw new RecommendationNotFoundException("ALS", userId);
        } else {
            return recList;
        }
    }

    @GetMapping(value = "/realtime/{userId}", produces = "application/json")
    UserRecList getRealtimeRecommendation(@PathVariable String userId) {
        String REALTIME_REC_COLLECTION = "realtime_recommendation";
        UserRecList recList = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), UserRecList.class, REALTIME_REC_COLLECTION);
        if (recList == null) {
            throw new RecommendationNotFoundException("realtime", userId);
        }
        return recList;
    }

    @GetMapping(value = "/itemcf/{productId}", produces = "application/json")
    ProductRecList getItemCFRecommendation(@PathVariable String productId) {
        String ITEMCF_REC_COLLECTION = "itemcf_recommendation";
        ProductRecList recList = mongoTemplate.findOne(
                Query.query(Criteria.where("productId").is(productId)), ProductRecList.class, ITEMCF_REC_COLLECTION);
        if (recList == null) {
            throw new RecommendationNotFoundException("itemCF", productId);
        }
        return recList;
    }

    @GetMapping(value = "/recommend_for_you/{userId}", produces = "application/json")
    UserRecList getRecommendForYou(@PathVariable String userId) {
        String TRENDING_REC_COLLECTION = "trending_recommendation";
        String ALS_REC_COLLECTION = "als_recommendation";
        UserRecList recList = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), UserRecList.class, ALS_REC_COLLECTION);
        if (recList == null) {
            List<RecommendItem> list = mongoTemplate.findAll(RecommendItem.class, TRENDING_REC_COLLECTION);
            recList = new UserRecList(userId, list);
            if (recList == null) {
                throw new RecommendationNotFoundException("trending", userId);
            }
        }
        return recList;
    }

    @GetMapping(value = "/guess_you_like/{userId}", produces = "application/json")
    UserRecList getGuessYouLike(@PathVariable String userId) {
        String TOPRATE_REC_COLLECTION = "top_rate_recommendation";
        String REALTIME_REC_COLLECTION = "realtime_recommendation";
        UserRecList recList = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), UserRecList.class, TOPRATE_REC_COLLECTION);
        if (recList == null) {
            List<RecommendItem> list = mongoTemplate.findAll(RecommendItem.class, REALTIME_REC_COLLECTION);
            recList = new UserRecList(userId, list);
            if (recList == null) {
                throw new RecommendationNotFoundException("top_rate", userId);
            }
        }
        return recList;
    }
}
