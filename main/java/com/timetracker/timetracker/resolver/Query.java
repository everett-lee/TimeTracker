package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.timetracker.timetracker.model.*;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
//import com.timetracker.timetracker.service.UserService;
import com.timetracker.timetracker.service.TimeCommitService;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Root query class that returns the Tasks owned
 * by the current user.
 */
@Component
public class Query implements GraphQLQueryResolver {
    private ClientService clientService;
    private TaskService taskService;
    private SubtaskService subtaskService;
    private TimeCommitService timeCommitService;

    @Autowired
    public Query(TaskService taskService, ClientService clientService, TimeCommitService timeCommitService, SubtaskService subtaskService) {
        this.taskService = taskService;
        this.subtaskService = subtaskService;
        this.clientService = clientService;
        this.timeCommitService = timeCommitService;
    }

    public List<Task> getAllTasks(Long ownerId) {
        return taskService.getAllTasksByOwnerId(ownerId);
    }

    public List<Client> getAllClients(Long ownerId) {
        return clientService.getAllClientsByOwnerId(ownerId);
    }

    public Set<Subtask> getAllSubtasks(Long ownerId, Long taskId) throws TaskNotFoundException {
        return subtaskService.getAllSubtasksByOwnerAndSubtaskIds(ownerId ,taskId);
    }

    public List<TimeCommit> getAllTimeCommits(Long ownerId, Long subtaskId) throws SubtaskNotFoundException {
        return timeCommitService.getAllTimeCommitsByOwnerAndSubtaskIds(ownerId ,subtaskId);
    }
}
