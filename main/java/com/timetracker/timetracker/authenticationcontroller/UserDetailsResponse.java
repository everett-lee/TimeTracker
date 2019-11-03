package com.timetracker.timetracker.authenticationcontroller;

import com.timetracker.timetracker.model.User;

/**
 * for object marshalling as HTTP response
 */
public class UserDetailsResponse {
    private Long id;
    private String email;

    public UserDetailsResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
