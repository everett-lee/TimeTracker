package com.timetracker.timetracker.service.exceptions;

public class SubtaskNotFoundException extends Exception {
    public SubtaskNotFoundException(Long id) {
        super(String
                .format("Subtask with id: %s does not exist", id));
    }
}
