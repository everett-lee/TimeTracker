package com.timetracker.timetracker.service.exceptions;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException(Long id) {
        super(String.format("Client with id: %s does not exist", id));
    }
}
