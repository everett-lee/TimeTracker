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

@Service
public class TimeCommitService {
    private TimeCommitRepository timeCommitRepo;
    private SubtaskRepository subtaskRepo;
    private static final long DAY_IN_SECONDS = 86400;

    private String ACCESS_DENIED_MESSAGE = "User does not have ownership of this ";

    @Autowired
    public TimeCommitService(TimeCommitRepository timeCommitRepo, SubtaskRepository subtaskRepo) {
        this.timeCommitRepo = timeCommitRepo;
        this.subtaskRepo = subtaskRepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public TimeCommit createOrUpdateTimeCommit(Long ownerId, Long subtaskId, Long time) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        if (!subtask.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "subtask");
        }

        // check if there is an existing time commit for the same date
        Optional<TimeCommit> existing = subtask
                .getTimeCommits()
                .stream().filter(tc -> tc.getDate().equals(LocalDate.now()))
                .findAny();

        TimeCommit timeCommit;

        // Update and return the existing time commit if not null
        if (existing.isPresent()) {
            timeCommit = existing.get();

            long newTime = calculateNewTime(timeCommit.getTime(), time);

            timeCommit.setTime(newTime);
            return timeCommit;
        }

        timeCommit = new TimeCommit();
        timeCommit.setDate(LocalDate.now());
        timeCommit.setTime(time);
        timeCommit.setOwnerId(ownerId);
        timeCommitRepo.save(timeCommit);

        List<TimeCommit> commits = subtask.getTimeCommits();
        commits.add(timeCommit);
        commits.sort(Comparator.comparing(tc -> tc.getDate()));

        return timeCommit;
    }

    private long calculateNewTime(long oldTime, long timeIn) {
        long newTime = oldTime + timeIn;

        // No more than one day in seconds can be added to a single
        // timecommit
        if (newTime > DAY_IN_SECONDS) {
            return DAY_IN_SECONDS;
        }

        return newTime;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public boolean deleteTimeCommit(Long ownerId, Long subtaskId, Long timeCommitId) throws TimeCommitNotFoundException, SubtaskNotFoundException {
        TimeCommit timeCommit = timeCommitRepo.findById(timeCommitId)
                .orElseThrow((() -> new TimeCommitNotFoundException(timeCommitId)));

        if (!timeCommit.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "TimeCommit");
        }

        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        subtask.getTimeCommits().removeIf(tc -> tc.getId().equals(timeCommitId));

        return true;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public TimeCommit updateTimeCommit(Long ownerId, Long timeCommitId, Long time) throws TimeCommitNotFoundException {
        TimeCommit timeCommit = timeCommitRepo.findById(timeCommitId)
                .orElseThrow((() -> new TimeCommitNotFoundException(timeCommitId)));

        if (!timeCommit.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "TimeCommit");
        }

        timeCommit.setTime(time);
        return timeCommit;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#ownerId == principal.id")
    public List<TimeCommit> getAllTimeCommitsByOwnerAndSubtaskIds(Long ownerId, Long subtaskId) throws SubtaskNotFoundException {
        Subtask subtask = subtaskRepo.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(subtaskId));

        if (!subtask.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE + "Subtask");
        }

        return subtask.getTimeCommits();
    }
}
