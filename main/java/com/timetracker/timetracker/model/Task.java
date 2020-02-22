package com.timetracker.timetracker.model;

import lombok.Data;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 *  Top level task. This represents the overall goal
 *  and has a single client and one or more subtasks.
 */
@Entity
@Data
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private Long ownerId;

    private String taskName;

    @OneToOne
    @JoinColumn(name = "client_fk")
    private Client client;

    private LocalDate dateAdded;

    private LocalDate dateCompleted;

    // the time committed to this subtask
    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Subtask> subtasks;

    private boolean completed;

    // Get sum of all timecommits of all subtasks
    public long getTotalTime() {
        return getSubtasks().stream()
        .map( subtask -> subtask.getTimeCommits())
        .flatMapToLong(timeCommits -> timeCommits.stream()
                .mapToLong(timeCommit -> timeCommit.getTime())).sum();
    }
}
