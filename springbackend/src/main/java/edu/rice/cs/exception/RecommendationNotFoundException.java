package edu.rice.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by joeyhaohao on 5/22/20
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="recommendation not found")
public class RecommendationNotFoundException extends RuntimeException {
    public RecommendationNotFoundException(String type, String id) {
        super("Could not find" + type + "recommendation for " + id);
    }
}
