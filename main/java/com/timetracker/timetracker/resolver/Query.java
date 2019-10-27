package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Root query class that returns the Tasks owned
 * by the current user.
 */
@Component
public class Query implements GraphQLQueryResolver {
    private TaskService taskService;
    public UserService userService;

    public Query(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public List<Task> getAllTasks(Long ownerId) {
        return taskService.getAllTasks(ownerId);
    }

    public User findUserByEmail(String email) {
        return userService.findbyEmail(email);
    }
}
