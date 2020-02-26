package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TaskResolver implements GraphQLResolver<Task> {
    private TaskService taskService;

    @Autowired
    public TaskResolver(TaskService taskService) {
        this.taskService = taskService;
    }

    // return the list of Subtasks associated
    // with this Task
    public Set<Subtask> subtasks(Task task) {
        return taskService.getSubtasksByTask(task);
    }

    // return the Client associated with this Task
    public Client client(Task task) {
        return taskService.getClientByTask(task);
    }
}
