package com.timetracker.timetracker.service;

import com.timetracker.timetracker.repository.UserRepository;
import com.timetracker.timetracker.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
/**
 * Implement the Spring Security UserDetailsService interface
 * so that credentials can be checked against the database
 * and UserDetails returned
 */
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;

    public JwtUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    // overrides only method
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }

        // return Spring User representation of the user
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}