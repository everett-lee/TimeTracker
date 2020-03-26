package com.timetracker.timetracker.service.exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class DeletedDependencyException extends Exception implements GraphQLError {
    public DeletedDependencyException(Long id) {
        super(String
                .format("Another Subtask is dependent on Subtask with id: %s", id));
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }
}
