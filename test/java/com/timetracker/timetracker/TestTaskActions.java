package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Checks that actions relating to modifying and retrieving
 * tasks can only be performed by owner
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestTaskActions {

    @Autowired
    ClientService clientService;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepository taskRepo;
    @Autowired
    ClientRepository clientRepo;


    // expect successful addition of task where user's id matches task
    // owner id
    @Test
    @WithMockCustomUser( id = 1L )
    public void testCreateTaskValidId() {
        String taskName = "Fix the rocket";
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, taskName, 1L);

        assertEquals(taskService.getAllTasksByOwnerId(1L).get(0).getTaskName(), taskName);
    }

    // expect access to be denied where user id does not match the task
    // owner id
    @Test (expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 4L )
    public void testCreateTaskInvalidId() {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Fix the rocket", 1L);
    }

    // expect successful addition of task where user's id matches task
    // owner id
    @Test
    @WithMockCustomUser( id = 1L )
    public void testOnlyOwnedTasksRetrieved() {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        clientService.createClient("Hat co", "Hat making", "Broadstairs");

        Task t1 = new Task();
        t1.setTaskName("Fix the rocket");
        t1.setClient(clientRepo.findById(1L).get());
        t1.setOwnerId(1L);
        Task t2 = new Task();
        t2.setTaskName("Summarise: what is a hat?");
        t2.setClient(clientRepo.findById(2L).get());
        t2.setOwnerId(1L);
        Task t3 = new Task();
        t3.setTaskName("Legal defence work (defamation)");
        t3.setClient(clientRepo.findById(1L).get());
        t3.setOwnerId(2L);

        taskRepo.save(t1);
        taskRepo.save(t2);
        taskRepo.save(t3);

        List<com.timetracker.timetracker.model.Task> result = taskService.getAllTasksByOwnerId(1L);

        // there are three tasks
        assertEquals(taskRepo.count(), 3);
        // remove any with wrong id
        result.stream().filter( t -> t.getOwnerId() == 1L);
        // assert tasks owned by this user is of the correct amount
        assertEquals(2, result.size());
    }
}


