package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TimeCommitRepository;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TimeCommitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeCommitService {
    private TimeCommitRepository timeCommitRepo;
    private SubtaskRepository subtaskRepo;

    private String ACCESS_DENIED_MESSAGE = "User does not have ownership of this ";

    @Autowired
    public TimeCommitService(TimeCommitRepository timeCommitRepo, SubtaskRepository subtaskRepo) {
        this.timeCommitRepo = timeCommitRepo;
        this.subtaskRepo = subtaskRepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public TimeCommit createTimeCommit(Long ownerId, Long subtaskId, Long time) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        if (subtask.getOwnerId() != ownerId) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "subtask");
        }

        // check if there is an existing time commit for the same date
        Optional<TimeCommit> existing = subtask
                .getTimeCommits()
                .stream().filter(tc -> tc.getDate().equals(LocalDate.now()))
                .findAny();

        // return the existing time commit if not null
        if (existing.isPresent()) {
            return existing.get();
        }

        TimeCommit timeCommit = new TimeCommit();
        timeCommit.setDate(LocalDate.now());
        timeCommit.setTime(time);
        timeCommit.setOwnerId(ownerId);
        timeCommit.setSubtask(subtask);
        timeCommitRepo.save(timeCommit);

        List<TimeCommit> commits = subtask.getTimeCommits();
        commits.add(timeCommit);
        commits.sort(Comparator.comparing(tc -> tc.getDate()));

        return timeCommit;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public boolean deleteTimeCommit(Long ownerId, Long subtaskId, Long timeCommitId) throws TimeCommitNotFoundException, SubtaskNotFoundException {
        TimeCommit timeCommit = timeCommitRepo.findById(timeCommitId)
                .orElseThrow((() -> new TimeCommitNotFoundException(timeCommitId)));

        if (timeCommit.getOwnerId() != ownerId) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "TimeCommit");
        }

        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        subtask.getTimeCommits().removeIf( tc -> tc.getId() == timeCommitId);

        return true;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public TimeCommit updateTimeCommit(Long ownerId, Long timeCommitId, Long time) throws TimeCommitNotFoundException {
        TimeCommit timeCommit = timeCommitRepo.findById(timeCommitId)
                .orElseThrow((() -> new TimeCommitNotFoundException(timeCommitId)));

        if (timeCommit.getOwnerId() != ownerId) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "TimeCommit");
        }

        timeCommit.setTime(time);
        return timeCommit;
    }
}
