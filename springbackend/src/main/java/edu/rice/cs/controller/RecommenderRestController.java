package edu.rice.cs.controller;

import edu.rice.cs.exception.RecommendationNotFoundException;
import edu.rice.cs.model.RecommendResult;
import edu.rice.cs.repositories.RecommendationRepository;
import edu.rice.cs.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommender")
public class RecommenderRestController {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecommenderService recommenderService;

    @GetMapping(value = "/cf/{userId}", produces = "application/json")
    RecommendResult getCFRecommendation(@PathVariable String userId) {
        return recommendationRepository.findByUserId(userId)
                .orElseThrow(() -> new RecommendationNotFoundException(userId));
//        return recommendationRepository.findAll();
    }

    @GetMapping(value = "/realtime/{userId}", produces = "application/json")
    RecommendResult getRealtimeRecommendation(@PathVariable String userId) {
        return recommenderService.getRealtimeRecommendation(userId);
    }
}
