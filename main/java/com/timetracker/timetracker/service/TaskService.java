package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepo;
    private ClientRepository clientRepo;

    public TaskService(TaskRepository taskRepo, ClientRepository clientRepo) {
        this.taskRepo = taskRepo;
        this.clientRepo = clientRepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Task createTask(Long ownerId, String taskName, Long clientId) {

        Task task = new Task();
        task.setOwnerId(ownerId);
        task.setTaskName(taskName);
        task.setClient(clientRepo.findById(clientId).get());
        task.setDateAdded(LocalDate.now());
        task.setCompleted(false);
        task.setSubtasks(new ArrayList<>());

        taskRepo.save(task);
        return task;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#ownerId == principal.id")
    public List<Task> getAllTasksByOwnerId(Long ownerId) {

        List<Task> tasks = taskRepo.findAllByOwnerId(ownerId);
        return tasks;
    }
}
