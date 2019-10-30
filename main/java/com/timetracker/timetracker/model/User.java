package com.timetracker.timetracker.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique=true)
    private String email;

    // no-arg constructor used by default
    public User() {
    }

    // constructor for mocking users in tests
    public User(long id, String email) {
        this.id = id;
        this.email = email;
        this.password = "password";
    }

    // stored as hash
    private String password;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
