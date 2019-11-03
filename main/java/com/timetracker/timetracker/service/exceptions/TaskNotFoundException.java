package com.timetracker.timetracker.service.exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(Long id) {
        super(String
                .format("Task with id: %s does not exist", id));
    }
}
