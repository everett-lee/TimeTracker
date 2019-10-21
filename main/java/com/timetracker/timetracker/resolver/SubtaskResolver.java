package com.timetracker.timetracker.resolver;


import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.repository.SubtaskRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubtaskResolver implements GraphQLResolver<Subtask> {
    private SubtaskRepository subtaskRepo;

    public SubtaskResolver(SubtaskRepository subtaskRepo) {
        this.subtaskRepo = subtaskRepo;
    }

    public List<Subtask> dependsOn(Subtask subtask) {
        List<Subtask> out = new ArrayList<>();

        for (Subtask s: subtask.getDependsOn()) {
            out.add(subtaskRepo.findById(s.getId()).get());
        }

        return out;
    }
}
