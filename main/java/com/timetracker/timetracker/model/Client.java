package com.timetracker.timetracker.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
