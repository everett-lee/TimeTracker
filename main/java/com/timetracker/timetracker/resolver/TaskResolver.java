package com.timetracker.timetracker.resolver;


import com.coxautodev.graphql.tools.GraphQLResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskResolver implements GraphQLResolver<Task> {
    private ClientRepository clientRepo;
    private SubtaskRepository subtaskRepo;

    public TaskResolver(ClientRepository clientRepo, SubtaskRepository subtaskRepo) {
        this.clientRepo = clientRepo;
        this.subtaskRepo = subtaskRepo;
    }

    public List<Subtask> subtasks(Task task) {
        List<Subtask> out = new ArrayList<>();

        for (Subtask subtask: task.getSubtasks()) {
            out.add(subtaskRepo
                    .findById(subtask.getId()).get());
        }

        return out;
    }

    public Client client(Client client) {
        return clientRepo.findById(client.getId()).get();
    }
}
