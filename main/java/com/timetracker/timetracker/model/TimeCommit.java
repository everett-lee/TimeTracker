package com.timetracker.timetracker.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents work done on a Subtask on a
 * specific date
 */
@Entity
@Table(name = "time_commit")
public class TimeCommit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "time_commit_id")
    private Long id;

    private Long ownerId;

    // date of the time commit
    private LocalDate date;

    // the time spent on the subtask
    // on this date in milliseconds
    private Long time;

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
