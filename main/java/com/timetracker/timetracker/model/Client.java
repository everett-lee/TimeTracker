package com.timetracker.timetracker.model;

import lombok.Data;

import javax.persistence.*;

/**
 * The client associated with a Task
 */
@Entity
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    private Long ownerId;

    private String clientName;

    private String businessType;

    private String location;
}
