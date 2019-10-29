package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByOwnerId(Long ownerId);
}
