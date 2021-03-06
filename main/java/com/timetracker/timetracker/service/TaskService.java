package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {
    private TaskRepository taskRepo;
    private SubtaskService subtaskService;
    private ClientRepository clientRepo;

    private String ACCESS_DENIED_MESSAGE = "User does not have ownership of this ";

    public TaskService(TaskRepository taskRepo, ClientRepository clientRepo,
                       SubtaskService subtaskService) {
        this.taskRepo = taskRepo;
        this.clientRepo = clientRepo;
        this.subtaskService = subtaskService;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Task createTask(Long ownerId, String taskName, Long clientId) throws ClientNotFoundException {

        Task task = new Task();
        task.setOwnerId(ownerId);
        task.setTaskName(taskName);

        task.setClient(clientRepo
                .findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId)));

        task.setDateAdded(LocalDate.now());
        task.setCompleted(false);
        task.setSubtasks(new HashSet<>());

        taskRepo.save(task);
        return task;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public boolean deleteTask(Long ownerId, Long taskId) throws TaskNotFoundException {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Task");
        }

        taskRepo.deleteById(taskId);
        return true;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Task setTaskComplete(Long ownerId, Long taskId, boolean complete) throws TaskNotFoundException {
        Task task = taskRepo
                .findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Task");
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

        return taskRepo.findAllByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#ownerId == principal.id")
    public Task getTaskById(Long ownerId, Long taskId) throws TaskNotFoundException {
        Task task = taskRepo
                .findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Task");
        }

        return task;
    }

    @Transactional(readOnly = true)
    public Set<Subtask> getSubtasksByTask(Task task) {
        return task.getSubtasks();
    }

    @Transactional(readOnly = true)
    public Client getClientByTask(Task task) {
        return task.getClient();
    }


    public long totalTime(Task task) {
        return task.getSubtasks().stream()
                .mapToLong(st -> subtaskService.totalTime(st))
                .sum();
    }
}
