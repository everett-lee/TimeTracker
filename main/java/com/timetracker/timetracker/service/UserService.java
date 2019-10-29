package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserRepository userRepo;
    // adds password hashing
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        userRepo.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User findbyEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
