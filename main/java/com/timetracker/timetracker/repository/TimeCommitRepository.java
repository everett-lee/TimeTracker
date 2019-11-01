package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.TimeCommit;
import org.springframework.data.repository.CrudRepository;

public interface TimeCommitRepository extends CrudRepository<TimeCommit, Long> {
}