package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.repository.TimeCommitRepository;
import com.timetracker.timetracker.service.exceptions.DeletedDependencyException;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubtaskService {

    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;

    private String ACCESS_DENIED_MESSAGE = "User does not have ownership of this ";

    public SubtaskService(SubtaskRepository subtaskRepo, TaskRepository taskrepo,
                          TimeCommitRepository timeCommitRepo) {
        this.subtaskRepo = subtaskRepo;
        this.taskRepo = taskrepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Subtask createSubtask(Long ownerId, Long taskId, String subtaskName,
                                 String category, List<Long> dependsOnIds) throws TaskNotFoundException, SubtaskNotFoundException {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Task");
        }

        Subtask subtask = new Subtask();
        subtask.setOwnerId(ownerId);
        subtask.setSubtaskName(subtaskName);
        subtask.setCategory(category);
        subtask.setDateAdded(LocalDate.now());
        subtask.setCompleted(false);
        subtask.setTimeCommits(new ArrayList<>());

        // add the subtask's dependencies
        Set<Subtask> dependsOn = new HashSet<>();
        for (Long id : dependsOnIds) {
            Subtask parent = subtaskRepo.findById(id)
                    .orElseThrow(() -> new SubtaskNotFoundException(id));

            dependsOn.add(parent);
        }
        subtask.setDependsOn(dependsOn);

        // update the owning task's subtasks to
        // include the new addition
        task.getSubtasks().add(subtask);
        subtask.setTask(task);

        return subtask;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public boolean deleteSubtask(Long ownerId, Long subtaskId) throws SubtaskNotFoundException, DeletedDependencyException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        Task task = subtask.getTask();

        if (!subtask.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Subtask");
        }

        // check if any subtask depends on the one that is to be deleted
        long dependencies = subtask.getDependents().size();
        if (dependencies > 0) {
            throw new DeletedDependencyException(subtaskId);
        }

        task.getSubtasks().removeIf(st -> st.getId().equals(subtaskId));
        return true;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Subtask setSubtaskComplete(Long ownerId, Long subtaskId, boolean complete) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        if (!subtask.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Subtask");
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
    public Set<Subtask> dependents(Subtask subtask) throws SubtaskNotFoundException {
        return subtask.getDependents();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#subtask.getOwnerId() == principal.id")
    public Set<Subtask> dependsOn(Subtask subtask) throws SubtaskNotFoundException {
        return subtask.getDependsOn();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#subtask.getOwnerId() == principal.id")
    public List<TimeCommit> timeCommits(Subtask subtask) {
        return subtask.getTimeCommits();
    }

    @Transactional
    @PreAuthorize("#subtask.getOwnerId() == principal.id")
    public long totalTime(Subtask subtask) {
        return subtask.getTimeCommits().stream()
                .mapToLong(tc -> tc.getTime())
                .sum();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#ownerId == principal.id")
    public Set<Subtask> getAllSubtasksByOwnerAndSubtaskId(Long ownerId, Long taskId) throws TaskNotFoundException {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Task");
        }

        return task.getSubtasks();
    }
}
