package com.timetracker.timetracker.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Extends the Spring Security User class to include id field.
 * This is done so that id can be recalled from the security context
 * when running a pre authorise check on method calls.
 */
public class CustomPrincipalUser extends org.springframework.security.core.userdetails.User {
    private Long id;

    public CustomPrincipalUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
