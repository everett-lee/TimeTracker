package com.timetracker.timetracker.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents work done on a Subtask on a
 * specific date
 */
@Entity
@Data
@Table(name = "time_commit")
public class TimeCommit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_commit_id")
    private Long id;

    private Long ownerId;

    // date of the time commit
    private LocalDate date;

    // the time spent on the subtask
    // on this date in milliseconds
    private Long time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeCommit that = (TimeCommit) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
