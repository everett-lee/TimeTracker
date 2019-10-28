package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * Handles actions that modify state in some way,
 * by creating or altering records.
 */
public class Mutation implements GraphQLMutationResolver {
    private TaskService taskservice;
    private SubtaskService subtaskService;
    private UserService userService;
    private ClientService clientService;

    public Mutation(TaskService taskService, SubtaskService subtaskService,
                    ClientService clientService, UserService userService) {
        this.subtaskService = subtaskService;
        this.taskservice = taskService;
        this.clientService = clientService;
        this.userService = userService;
    }

    public Task createTask(Long ownerId, String taskName, Long clientId) {
        return taskservice.createTask(ownerId, taskName, clientId);
    }

    public Subtask createSubtask(String subtaskName, String category, List<Long> dependsOnIds) {
        return subtaskService.createSubtask(subtaskName, category, dependsOnIds);
    }

    public Client createClient(String clientName, String businessType, String location) {
        return clientService.createClient(clientName, businessType, location);
    }

    public User createUser(String email, String password) {
        return userService.createUser(email, password);
    }

    public Subtask setSubtaskTime(Long subtaskId, Long time) {
        return subtaskService.setSubtaskTime(subtaskId, time);
    }

    public Subtask setSubtaskComplete(Long subtaskId, boolean complete) {
        return subtaskService.setSubtaskComplete(subtaskId, complete);
    }
}
