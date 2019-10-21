package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Subtask;
import org.springframework.data.repository.CrudRepository;

public interface SubtaskRepository extends CrudRepository<Subtask, Long> {
}
