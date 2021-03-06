package edu.rice.cs.security;

import edu.rice.cs.exception.UserNotFoundException;
import edu.rice.cs.model.User;
import edu.rice.cs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * Created by songxiongfeng on 5/27/20
 */
@Service
public class MongoUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public MongoUserDetailsService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

//        user.encodePassword(user.getPassword());

        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user")); // user

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

    }
}
