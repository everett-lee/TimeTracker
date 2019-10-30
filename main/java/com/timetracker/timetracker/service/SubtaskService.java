package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubtaskService {
    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;

    public SubtaskService(SubtaskRepository subtaskRepo, TaskRepository taskrepo) {
        this.subtaskRepo = subtaskRepo;
        this.taskRepo = taskrepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Subtask createSubtask(Long ownerId, Long taskId, String subtaskName,
                                 String category, List<Long> dependsOnIds) {

        Task task = taskRepo.findById(taskId).get();

        if (task.getOwnerId() != ownerId) {
            throw new AccessDeniedException("User does not have ownership of this Task");
        }

        Subtask subtask = new Subtask();
        subtask.setSubtaskName(subtaskName);
        subtask.setCategory(category);
        subtask.setDateAdded(LocalDate.now());
        subtask.setCompleted(false);
        subtask.setTotalTime(0L);

        // add the subtask's dependencies
        List<Subtask> dependsOn = dependsOnIds.stream()
                .map( id -> subtaskRepo.findById(id).get())
                .collect(Collectors.toList());
        subtask.setDependsOn(dependsOn);

        // update the owning task's subtasks to
        // include the new addition
        List<Subtask> subtasks = task.getSubtasks();
        subtasks.add(subtask);
        task.setSubtasks(subtasks);

        subtaskRepo.save(subtask);
        return subtask;
    }

    @Transactional
    public Subtask setSubtaskTime(Long subtaskId, Long time) {
        Subtask subtask = subtaskRepo.findById(subtaskId).get();
        subtask.setTotalTime(time);
        return subtask;
    }

    @Transactional
    public Subtask setSubtaskComplete(Long subtaskId, boolean complete) {
        Subtask subtask = subtaskRepo.findById(subtaskId).get();
        subtask.setCompleted(complete);
        return subtask;
    }

    @Transactional(readOnly = true)
    public List<Subtask> dependsOn(Subtask subtask) {
        List<Subtask> out = new ArrayList<>();

        for (Subtask s: subtask.getDependsOn()) {
            out.add(subtaskRepo.findById(s.getId()).get());
        }

        return out;
    }
}
