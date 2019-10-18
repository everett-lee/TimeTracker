package com.timetracker.timetracker.repository;

import com.timetracker.timetracker.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {}
