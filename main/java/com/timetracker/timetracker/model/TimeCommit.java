package com.timetracker.timetracker.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents work done on a Subtask on a
 * specific date
 */
@Entity
@Data
@Table(name = "time_commit")
public class TimeCommit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "time_commit_id")
    private Long id;

    private Long ownerId;

    @ManyToOne
    @JoinColumn(name="subtask_fk")
    private Subtask subtask;

    // date of the time commit
    private LocalDate date;

    // the time spent on the subtask
    // on this date in milliseconds
    private Long time;
}
