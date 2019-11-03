package com.timetracker.timetracker.authenticationcontroller;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String email) {
        super(String.format("User with email %s already exists", email));
    }
}
