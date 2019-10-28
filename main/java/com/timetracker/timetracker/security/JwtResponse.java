package com.timetracker.timetracker.security;

import java.io.Serializable;

/**
 * Contains token response that is sent
 * back to the client
 */
public class JwtResponse implements Serializable {
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}