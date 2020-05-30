package edu.rice.cs.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncoderService {

    private static PasswordEncoder passwordEncoder;

    public EncoderService() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public static PasswordEncoder getEncoder() {
        return passwordEncoder;
    }
}
