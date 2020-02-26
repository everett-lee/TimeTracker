package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Subtask;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface SubtaskRepository extends CrudRepository<Subtask, Long> {
    Set<Subtask> findAllByOwnerId(Long ownerId);
}
