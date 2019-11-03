package com.timetracker.timetracker.security;

import java.io.Serializable;

/**
 * Encapsulates user's log in details sent
 * by the client to the authentication endpoint
 */
public class JwtRequest implements Serializable {
    private String email;
    private String password;

    // need default constructor for JSON Parsing
    public JwtRequest() {
    }

    public JwtRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}