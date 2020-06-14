package edu.rice.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by songxiongfeng on 5/22/20
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="product not found")
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Could not find product " + productId);
    }
}
