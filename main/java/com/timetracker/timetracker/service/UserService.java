package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserRepository userRepo;

    @Transactional(readOnly = true)
    public User findbyEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
