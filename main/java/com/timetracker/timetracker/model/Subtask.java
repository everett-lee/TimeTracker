package com.timetracker.timetracker.model;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(name = "subtask_dependentsubtask",
            joinColumns = {@JoinColumn(name = "main_subtasktask_fk")},
            inverseJoinColumns = {@JoinColumn(name = "dependent_subtask_fk")}
    )
    private List<Subtask> dependsOn;

    @ManyToMany(mappedBy = "dependsOn")
    private List<Subtask> dependents;

    @Override
    public String toString() {
        return String.format("Name: %s Category: %s", this.subtaskName, this.category);
    }
}
