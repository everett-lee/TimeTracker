package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.TaskService;
//import com.timetracker.timetracker.service.UserService;
import com.timetracker.timetracker.service.TimeCommitService;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TimeCommitService timeCommitService;

    @Autowired
    public Query(TaskService taskService, ClientService clientService, TimeCommitService timeCommitService) {
        this.taskService = taskService;
        this.clientService = clientService;
        this.timeCommitService = timeCommitService;
    }

    public List<Task> getAllTasks(Long ownerId) {
        return taskService.getAllTasksByOwnerId(ownerId);
    }

    public List<Client> getAllClients(Long ownerId) {
        return clientService.getAllClientsByOwnerId(ownerId);
    }

    public List<TimeCommit> getAllTimeCommits(Long ownerId, Long subtaskId) throws SubtaskNotFoundException {
        return timeCommitService.getAllTimeCommitsByOwnerAndSubtaskIds(ownerId ,subtaskId);
    }
}
