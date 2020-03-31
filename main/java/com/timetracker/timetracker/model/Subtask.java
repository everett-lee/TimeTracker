package com.timetracker.timetracker.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Subtasks define the work involved in completing
 * a Task. Each has an associated category.
 */
@Entity
@Data
@Table(name = "subtask")
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private Long id;

    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "task_fk")
    private Task task;

    private String subtaskName;

    private String category;

    private LocalDate dateAdded;

    private LocalDate dateCompleted;

    private boolean completed;

    // the time committed to this subtask
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @JoinColumn(name = "subtask_fk")
    private List<TimeCommit> timeCommits;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subtask_dependentsubtask",
            joinColumns = {@JoinColumn(name = "main_subtasktask_fk")},
            inverseJoinColumns = {@JoinColumn(name = "dependent_subtask_fk")}
    )
    private Set<Subtask> dependsOn;

    @ManyToMany(mappedBy = "dependsOn")
    private Set<Subtask> dependents;

    @Override
    public String toString() {
        return String.format("Name: %s Category: %s", this.subtaskName, this.category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return id.equals(subtask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
