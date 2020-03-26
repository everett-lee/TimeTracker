package com.timetracker.timetracker.service.exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class DeletedClientWithTaskException extends Exception implements GraphQLError {
    public DeletedClientWithTaskException() {
        super(String
                .format("An existing Task is linked to this client"));
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
