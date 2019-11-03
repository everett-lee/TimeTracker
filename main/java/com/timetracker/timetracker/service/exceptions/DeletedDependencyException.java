package com.timetracker.timetracker.service.exceptions;

public class DeletedDependencyException extends Exception {
    public DeletedDependencyException(Long id) {
        super(String
                .format("Another Subtask is dependent on Subtask with id: %s", id));
    }
}
