package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.CustomPrincipalUser;
import com.timetracker.timetracker.repository.UserRepository;
import com.timetracker.timetracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
/**
 * Implement the Spring Security UserDetailsService interface
 * so that credentials can be checked against the database
 * and UserDetails returned
 */
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public JwtUserDetailsService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPrincipalUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }

        // return Spring Security User representation of the user (with no roles)
        return new CustomPrincipalUser(user.getId(), user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    @Transactional
    // save user to the database
    public User save(User user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        // TODO: implement custom exception response for duplicate email / breach of integrity constraint
        userRepo.save(newUser);

        return newUser;
    }
}