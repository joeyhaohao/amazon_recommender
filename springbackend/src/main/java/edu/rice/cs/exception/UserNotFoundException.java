package edu.rice.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by joeyhaohao on 5/24/20
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="user not found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Could not find user " + username);
    }
}
