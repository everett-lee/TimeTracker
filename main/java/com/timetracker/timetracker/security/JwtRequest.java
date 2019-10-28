package com.timetracker.timetracker.security;

import java.io.Serializable;

/**
 * Encapsulates user's log in details sent
 * by the client to the authentication endpoint
 */
public class JwtRequest implements Serializable {
    private String username;
    private String password;

    //need default constructor for JSON Parsing
    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}