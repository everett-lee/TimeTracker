package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SubtaskResolver implements GraphQLResolver<Subtask> {
    private SubtaskService subtaskService;

    public SubtaskResolver(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    // return the list of Subtasks that this Subtask
    // depends on
    public Set<Subtask> dependsOn(Subtask subtask) throws SubtaskNotFoundException {
        return subtaskService.dependsOn(subtask);
    }

    // return the list of TimeCommits associated with this subtask
    public List<TimeCommit> timeCommits(Subtask subtask) {
        return subtaskService.timeCommits(subtask);
    }

    // return the sum of TimeCommit times associated with this task;
    public Long totalTime(Subtask subtask) {
        return subtaskService.totalTime(subtask);
    }
}
