package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.repository.TimeCommitRepository;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubtaskService {
    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;
    private TimeCommitRepository timeCommitRepo;

    public SubtaskService(SubtaskRepository subtaskRepo, TaskRepository taskrepo,
                          TimeCommitRepository timeCommitRepo) {
        this.subtaskRepo = subtaskRepo;
        this.taskRepo = taskrepo;
        this.timeCommitRepo = timeCommitRepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Subtask createSubtask(Long ownerId, Long taskId, String subtaskName,
                                 String category, List<Long> dependsOnIds) throws TaskNotFoundException, SubtaskNotFoundException {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String
                        .format("Task with id: % does not exist", taskId)));

        if (task.getOwnerId() != ownerId) {
            throw new AccessDeniedException("User does not have ownership of this Task");
        }

        Subtask subtask = new Subtask();
        subtask.setOwnerId(ownerId);
        subtask.setSubtaskName(subtaskName);
        subtask.setCategory(category);
        subtask.setDateAdded(LocalDate.now());
        subtask.setCompleted(false);
        subtask.setTimeCommits(new ArrayList<>());
        subtask.setTotalTime(0L);

        // add the subtask's dependencies
        List<Subtask> dependsOn = new ArrayList<>();
        for (Long id: dependsOnIds) {
            dependsOn.add(subtaskRepo.findById(id)
                    .orElseThrow(() -> new SubtaskNotFoundException(String
                            .format("Subtask with id: %s does not exist", id))));
        }
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
    @PreAuthorize("#ownerId == principal.id")
    public Subtask setSubtaskTime(Long ownerId, Long subtaskId, Long time) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(String
                        .format("Subtask with id: %s does not exist", subtaskId)));

        if (subtask.getOwnerId() != ownerId) {
            throw new AccessDeniedException("User does not have ownership of this Task");
        }

        subtask.setTotalTime(time);
        return subtask;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Subtask setSubtaskComplete(Long ownerId, Long subtaskId, boolean complete) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(String
                        .format("Subtask with id: %s does not exist", subtaskId)));

        if (subtask.getOwnerId() != ownerId) {
            throw new AccessDeniedException("User does not have ownership of this Task");
        }

        if (complete) {
            subtask.setDateCompleted(LocalDate.now());
        // if setting from complete -> not complete date should be set to null
        } else {
            subtask.setDateCompleted(null);
        }

        subtask.setCompleted(complete);
        return subtask;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#subtask.getOwnerId() == principal.id")
    public List<Subtask> dependsOn(Subtask subtask) throws SubtaskNotFoundException {
        return subtask.getDependsOn();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#subtask.getOwnerId() == principal.id")
    public List<TimeCommit> timeCommits(Subtask subtask) {
        return subtask.getTimeCommits();
    }
}
