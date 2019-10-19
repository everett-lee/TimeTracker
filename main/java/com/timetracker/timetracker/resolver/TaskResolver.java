package com.timetracker.timetracker.resolver;


import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;

import java.util.List;


public class TaskResolver implements GraphQLResolver<Task> {
    private SubtaskRepository subtaskRepo;
    private ClientRepository clientRepo;

    public TaskResolver(SubtaskRepository subtaskRepo, ClientRepository clientRepo) {
        this.subtaskRepo = subtaskRepo;
        this.clientRepo = clientRepo;
    }

    public List<Subtask> subtasks(Task task) {
        return subtaskRepo.findByTask(task);
    }

    public Client client(Task task) {
        return clientRepo.findByTask(task);
    }
}
