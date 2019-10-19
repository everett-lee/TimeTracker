package com.timetracker.timetracker.resolver;


import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.repository.SubtaskRepository;

import java.util.List;


public class SubtaskResolver implements GraphQLResolver<Subtask> {
    private SubtaskRepository subtaskRepo;

    public SubtaskResolver(SubtaskRepository subtaskRepo) {
        this.subtaskRepo = subtaskRepo;
    }

    public List<Subtask> dependsOn(Subtask subtask) {
        return subtaskRepo.findBySubtask(subtask);
    }
}
