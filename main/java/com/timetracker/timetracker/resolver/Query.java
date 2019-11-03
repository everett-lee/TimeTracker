package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.TaskService;
//import com.timetracker.timetracker.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Root query class that returns the Tasks owned
 * by the current user.
 */
@Component
public class Query implements GraphQLQueryResolver {
    private TaskService taskService;
    private ClientService clientService;

    public Query(TaskService taskService, ClientService clientService) {
        this.taskService = taskService;
        this.clientService = clientService;
    }

    public List<Task> getAllTasks(Long ownerId) {
        return taskService.getAllTasksByOwnerId(ownerId);
    }

    public List<Client> getAllClients(Long ownerId) {
        return clientService.getAllClientsByOwnerId(ownerId);
    }
}
