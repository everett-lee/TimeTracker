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

    private Long taskId;

    private String subtaskName;

    private String category;

    private LocalDate dateAdded;

    private LocalDate dateCompleted;

    private boolean completed;

    // the time committed to this subtask
    @OneToMany(
            mappedBy = "subtask",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TimeCommit> timeCommits;

    // the subtasks this subtask depends on
    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "subtask_dependentsubtask",
            joinColumns = @JoinColumn(
                    name = "main_subtasktask_fk"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "dependent_subtask_fk"
            ))
    private List<Subtask> dependsOn;

}
