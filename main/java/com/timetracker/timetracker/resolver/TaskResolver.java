package com.timetracker.timetracker.resolver;


import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.repository.SubtaskRepository;

import java.util.List;


public class TaskResolver implements GraphQLResolver<Task> {
    private SubtaskRepository subtaskRepo;

    public TaskResolver(SubtaskRepository subtaskRepo) {
        this.subtaskRepo = subtaskRepo;
    }

    public List<Subtask> subtasks(Task task) {
        return subtaskRepo.findByTaskId(task.getId());
    }
}
