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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MutationResolver implements GraphQLMutationResolver {
    private ClientRepository clientRepo;
    private SubtaskRepository subtaskRepo;
    private TaskRepository taskRepo;
    private UserRepository userRepo;

    public MutationResolver(ClientRepository clientRepo, SubtaskRepository subtaskRepo
                            , TaskRepository taskRepo, UserRepository userRepo) {
        this.clientRepo = clientRepo;
        this.subtaskRepo = subtaskRepo;
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }


    public Task createTask(Long ownerId, String taskName, Client client) {

        Task task = new Task();
        task.setOwnerId(ownerId);
        task.setTaskName(taskName);
        task.setClient(client);
        task.setDateAdded(LocalDate.now());
        task.setCompleted(false);
        task.setSubtasks(new ArrayList<>());

        taskRepo.save(task);
        return task;
    }

    public Subtask createSubtask(String subtaskName, String category, List<Subtask> dependsOn) {

        Subtask subtask = new Subtask();
        subtask.setSubtaskName(subtaskName);
        subtask.setCategory(category);
        subtask.setDateAdded(LocalDate.now());
        subtask.setCompleted(false);
        subtask.setTotalTime(0L);
        subtask.setDependsOn(dependsOn);

        subtaskRepo.save(subtask);
        return subtask;
    }

    public Client create

    public User createUser() {
        User user = new User();

        userRepo.save(user);
        return user;
    }


}
