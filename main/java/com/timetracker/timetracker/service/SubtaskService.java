package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.repository.SubtaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubtaskService {
    private SubtaskRepository subtaskRepo;

    public SubtaskService(SubtaskRepository subtaskRepo) {
        this.subtaskRepo = subtaskRepo;
    }

    @Transactional
    public Subtask createSubtask(String subtaskName, String category, List<Long> dependsOnIds) {
        Subtask subtask = new Subtask();
        subtask.setSubtaskName(subtaskName);
        subtask.setCategory(category);
        subtask.setDateAdded(LocalDate.now());
        subtask.setCompleted(false);
        subtask.setTotalTime(0L);

        List<Subtask> dependsOn = dependsOnIds.stream()
                .map( id -> subtaskRepo.findById(id).get())
                .collect(Collectors.toList());

        subtask.setDependsOn(dependsOn);

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
