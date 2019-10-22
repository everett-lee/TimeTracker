package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        userRepo.save(user);
        return user;
    }
}
