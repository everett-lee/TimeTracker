package com.timetracker.timetracker.authenticationcontroller;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String username) {
        super(String.format("User with username %s already exists", username));
    }
}
