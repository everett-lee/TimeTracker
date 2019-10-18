package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Subtask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubtaskRepository extends CrudRepository<Subtask, Long> {

    List<Subtask> findByTaskId(Long id);
}
