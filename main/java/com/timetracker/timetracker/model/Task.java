package com.timetracker.timetracker.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Top level task. This represents the overall goal
 * and has a single client and one or more subtasks.
 */
@Entity
@Data
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private Long ownerId;

    private String taskName;

    @OneToOne
    @JoinColumn(name = "client_fk")
    private Client client;

    private LocalDate dateAdded;

    private LocalDate dateCompleted;

    // the subtasks associated with this subtask
    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Subtask> subtasks = new HashSet<>();

    private boolean completed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
