package com.timetracker.timetracker.service.exceptions;

public class TimeCommitNotFoundException extends Exception {
    public TimeCommitNotFoundException(Long id) {
        super(String
                .format("TimeCommit with id: %s does not exist", id));
    }
}
