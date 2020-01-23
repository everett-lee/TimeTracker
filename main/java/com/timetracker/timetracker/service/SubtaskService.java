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
import java.util.List;

@Service
public class SubtaskService {
    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;
    private TimeCommitRepository timeCommitRepo;

    private String ACCESS_DENIED_MESSAGE = "User does not have ownership of this ";

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
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getOwnerId() != ownerId) {
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
        List<Subtask> dependsOn = new ArrayList<>();
        for (Long id: dependsOnIds) {
            dependsOn.add(subtaskRepo.findById(id)
                    .orElseThrow(() -> new SubtaskNotFoundException(id)));
        }
        subtask.setDependsOn(dependsOn);

        // update the owning task's subtasks to
        // include the new addition
        task.getSubtasks().add(subtask);

        subtaskRepo.save(subtask);
        return subtask;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public boolean deleteSubtask(Long ownerId, Long subtaskId) throws SubtaskNotFoundException, DeletedDependencyException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        if (subtask.getOwnerId() != ownerId) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE +  "Subtask");
        }

        // check if any subtask depends on the one that is to be deleted
        long dependencies = subtaskRepo.findByOwnerId(ownerId)
                // don't include this subtask
                .stream().filter( el -> el.getId() != subtaskId)
                // map each subtask to its dependencies
                .map( st -> st.getDependsOn())
                .flatMap( arr -> arr.stream())
                .count();

        if (dependencies > 0) {
            throw new DeletedDependencyException(subtaskId);
        }

        subtaskRepo.deleteById(subtaskId);
        return true;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Subtask setSubtaskComplete(Long ownerId, Long subtaskId, boolean complete) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        if (subtask.getOwnerId() != ownerId) {
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
    public List<Subtask> dependsOn(Subtask subtask) throws SubtaskNotFoundException {
        return subtask.getDependsOn();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#subtask.getOwnerId() == principal.id")
    public List<TimeCommit> timeCommits(Subtask subtask) {
        return subtask.getTimeCommits();
    }
}
