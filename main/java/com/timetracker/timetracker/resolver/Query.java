package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.service.TaskService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Root query class that returns the Tasks owned
 * by the current user.
 */
@Component
public class Query implements GraphQLQueryResolver {
    private TaskService taskService;

    public Query(TaskService taskService) {
        this.taskService = taskService;
    }

    public List<Task> getAllTasks(Long id) {
        return taskService.getAllTasks(id);
    }
}
