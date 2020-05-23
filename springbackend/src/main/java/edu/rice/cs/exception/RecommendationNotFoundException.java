package edu.rice.cs.exception;

/**
 * Created by joeyhaohao on 5/22/20
 */
public class RecommendationNotFoundException extends RuntimeException {
    public RecommendationNotFoundException(String userId) {
        super("Could not find recommendaiton for user " + userId);
    }
}
