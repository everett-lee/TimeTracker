package com.timetracker.timetracker.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 *  Top level task. This represents the overall goal
 *  and has a single client and one or more subtasks.
 */
@Entity
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

    // the Subtasks that compose this Task
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn( name = "task_fk" )
    private List<Subtask> subtasks;

    private boolean completed;

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
