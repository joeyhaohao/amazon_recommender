package edu.rice.cs.exception;

/**
 * Created by joeyhaohao on 5/24/20
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Could not find user " + username);
    }
}
