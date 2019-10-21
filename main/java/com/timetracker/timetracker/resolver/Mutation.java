package com.timetracker.timetracker.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class Mutation implements GraphQLMutationResolver {
    private ClientRepository clientRepo;
    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;
    private UserRepository userRepo;

    public Mutation(ClientRepository clientRepo, SubtaskRepository subtaskRepo
                            , TaskRepository taskRepo, UserRepository userRepo) {
        this.clientRepo = clientRepo;
        this.subtaskRepo = subtaskRepo;
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }


    public Task createTask(Long ownerId, String taskName, Long clientId) {

        Task task = new Task();
        task.setOwnerId(ownerId);
        task.setTaskName(taskName);
        Client client = clientRepo.findById(clientId).get();
        task.setClient(client);
        task.setDateAdded(LocalDate.now());
        task.setCompleted(false);
        task.setSubtasks(new ArrayList<>());

        taskRepo.save(task);
        return task;
    }

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

    public Client createClient(String clientName, String businessType, String location) {

        Client client = new Client();
        client.setClientName(clientName);
        client.setBusinessType(businessType);
        client.setLocation(location);

        clientRepo.save(client);

        return client;
    }

    public User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        userRepo.save(user);
        return user;
    }


}
