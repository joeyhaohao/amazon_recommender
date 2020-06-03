package edu.rice.cs.exception;

/**
 * Created by songxiongfeng on 5/22/20
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Could not find product " + productId);
    }
}
