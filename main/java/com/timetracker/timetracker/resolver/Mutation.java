package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
//import com.timetracker.timetracker.service.UserService;
import com.timetracker.timetracker.service.exceptions.*;
import com.timetracker.timetracker.service.TimeCommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * handles actions that modify state in some way,
 * by creating, deleting or altering records.
 */
public class Mutation implements GraphQLMutationResolver {
    private TaskService taskservice;
    private SubtaskService subtaskService;
    private ClientService clientService;
    private TimeCommitService timeCommitService;

    @Autowired
    public Mutation(TaskService taskService, SubtaskService subtaskService,
                    ClientService clientService,
                    TimeCommitService timeCommitService) {
        this.subtaskService = subtaskService;
        this.taskservice = taskService;
        this.clientService = clientService;
        this.timeCommitService = timeCommitService;
    }

    // methods for creating and updating client entities

    public Client createClient(Long ownerId, String clientName, String businessType, String location) {
        return clientService.createClient(ownerId, clientName, businessType, location);
    }

    public boolean deleteClient(Long ownerId, Long clientId) throws ClientNotFoundException {
        return clientService.deleteClient(ownerId, clientId);
    }

    // methods for creating and updating task entities

    public Task createTask(Long ownerId, String taskName, Long clientId) throws ClientNotFoundException {
        return taskservice.createTask(ownerId, taskName, clientId);
    }

    public boolean deleteTask(Long ownerId, Long taskId) throws TaskNotFoundException {
        return taskservice.deleteTask(ownerId, taskId);
    }

    public Task setTaskComplete(Long ownerId, Long taskId, boolean complete) throws TaskNotFoundException {
        return taskservice.setTaskComplete(ownerId, taskId, complete);
    }

    // methods for creating and updating subtask entities

    public Subtask createSubtask(Long ownerId, Long taskId, String subtaskName,
                                 String category, List<Long> dependsOnIds) throws TaskNotFoundException, SubtaskNotFoundException {
        return subtaskService.createSubtask(ownerId, taskId, subtaskName, category, dependsOnIds);
    }

    public boolean deleteSubtask(Long ownerId, Long subtaskId) throws SubtaskNotFoundException, DeletedDependencyException {
        return  subtaskService.deleteSubtask(ownerId, subtaskId);
    }
    public Subtask setSubtaskComplete(Long ownerId, Long subtaskId, boolean complete) throws SubtaskNotFoundException {
        return subtaskService.setSubtaskComplete(ownerId, subtaskId, complete);
    }

    // methods for creating and updating timecommit entities

    public TimeCommit createTimeCommit(Long ownerId, Long subtaskId) throws SubtaskNotFoundException {
        return timeCommitService.createTimeCommit(ownerId, subtaskId);
    }

    public boolean deleteTimeCommit(Long ownerId, Long timeCommitId) throws TimeCommitNotFoundException {
        return timeCommitService.deleteTimeCommit(ownerId, timeCommitId);
    }

    public TimeCommit updateTime(Long ownerId, Long timeCommitId, Long time) throws TimeCommitNotFoundException {
        return timeCommitService.updateTime(ownerId, timeCommitId, time);
    }
}
