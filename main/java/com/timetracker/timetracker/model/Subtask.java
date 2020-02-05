package com.timetracker.timetracker.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Subtasks define the work involved in completing
 * a Task. Each has an associated category.
 */
@Entity
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

    public String getSubtaskName() {
        return subtaskName;
    }

    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<Subtask> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<Subtask> dependsOn) {
        this.dependsOn = dependsOn;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public List<TimeCommit> getTimeCommits() {
        return timeCommits;
    }

    public void setTimeCommits(List<TimeCommit> timeCommits) {
        this.timeCommits = timeCommits;
    }
}
