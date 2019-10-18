package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Root query class that returns the Tasks owned
 * by the current user.
 */
public class QueryResolver implements GraphQLQueryResolver {
    private TaskRepository taskRepo;

    public QueryResolver(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> getAllTasks(Long id) {
        List<Task> tasks = new ArrayList<>();

        for (Task t: taskRepo.findAll()) {
            if (t.getOwnerId() == id) {
                tasks.add(t);
            }
        }

        return tasks;
    }
}
