package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepo;
    private SubtaskRepository subtaskRepo;
    private ClientRepository clientRepo;

    public TaskService(TaskRepository taskRepo, ClientRepository clientRepo,
                       SubtaskRepository subtaskRepo) {
        this.taskRepo = taskRepo;
        this.clientRepo = clientRepo;
        this.subtaskRepo = subtaskRepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Task createTask(Long ownerId, String taskName, Long clientId) throws ClientNotFoundException {

        Task task = new Task();
        task.setOwnerId(ownerId);
        task.setTaskName(taskName);

        task.setClient(clientRepo
                .findById(clientId)
                .orElseThrow(() ->
                        new ClientNotFoundException(String.format("Client with id: %s does not exist", clientId))));

        task.setDateAdded(LocalDate.now());
        task.setCompleted(false);
        task.setSubtasks(new ArrayList<>());

        taskRepo.save(task);
        return task;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Task setTaskComplete(Long ownerId, Long taskId, boolean complete) throws TaskNotFoundException {

        Task task = taskRepo
                .findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String
                        .format("Task with id: % does not exist", taskId)));

        if (task.getOwnerId() != ownerId) {
            throw new AccessDeniedException("User does not have ownership of this Task");
        }

        if (complete) {
            task.setDateCompleted(LocalDate.now());
            // if setting from complete -> not complete date should be null
        } else {
            task.setDateCompleted(null);
        }

        task.setCompleted(complete);

        return task;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#ownerId == principal.id")
    public List<Task> getAllTasksByOwnerId(Long ownerId) {

        List<Task> tasks = taskRepo.findAllByOwnerId(ownerId);
        return tasks;
    }
}
