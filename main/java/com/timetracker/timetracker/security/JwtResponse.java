package com.timetracker.timetracker.security;

import java.io.Serializable;

/**
 * Contains token and id response that is sent
 * back to the client
 */
public class JwtResponse implements Serializable {
    private final String jwttoken;
    private final Long id;

    public JwtResponse(String jwttoken, Long id) {
        this.jwttoken = jwttoken;
        this.id = id;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Long getId() {
        return id;
    }
}