package com.timetracker.timetracker;


import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Checks that actions relating to modifying and retrieving
 * tasks are performed correctly
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestTaskActions {

    @Autowired
    ClientService clientService;
    @Autowired
    TaskService taskService;
    @Autowired
    SubtaskService subtaskService;
    @Autowired
    TaskRepository taskRepo;
    @Autowired
    ClientRepository clientRepo;
    @Autowired
    SubtaskRepository subtaskRepository;

    // expect successful addition of task where user's id matches task
    // owner id
    @Test
    @WithMockCustomUser(id = 1L)
    public void testCreateTaskValidId() throws ClientNotFoundException {
        String taskName = "Fix the rocket";
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, taskName, 1L);

        assertEquals(taskService.getAllTasksByOwnerId(1L).get(0).getTaskName(), taskName);
    }

    // expect access to be denied where user id does not match the task
    // owner id
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(id = 4L)
    public void testCreateTaskInvalidId() throws ClientNotFoundException {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Fix the rocket", 1L);
    }

    // expect successful addition of task where user's id matches task
    // owner id
    @Test
    @WithMockCustomUser(id = 1L)
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

        // there are three tasks in the repo
        assertEquals(taskRepo.count(), 3);
        // assert tasks owned by this user is of the correct amount
        assertEquals(2, result.size());
        // remove any with wrong id and check count is the same
        assertEquals(2, result.stream().filter(t -> t.getOwnerId() == 1L).count());
    }


    // expect to succeed as user with id 1 owns this task
    @Test
    @WithMockCustomUser(id = 1L)
    public void testSetCompleted() throws ClientNotFoundException, TaskNotFoundException {
        String taskName = "Fix the rocket";
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, taskName, 1L);
        taskService.setTaskComplete(1L, 1L, true);

        assertEquals(true, taskRepo.findAllByOwnerId(1L).get(0).isCompleted());
    }

    // expect to raise exception as user with id 1 should
    // not alter tasks by using owner id 2
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(id = 1L)
    public void testSetCompletedWhereWrongUserIdUsed() throws TaskNotFoundException {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        clientService.createClient("Hat co", "Hat making", "Broadstairs");

        Task t1 = new Task();
        t1.setTaskName("Fix the rocket");
        t1.setClient(clientRepo.findById(1L).get());
        t1.setOwnerId(1L);
        Task t2 = new Task();
        t2.setTaskName("Summarise: what is a hat?");
        t2.setClient(clientRepo.findById(2L).get());
        t2.setOwnerId(2L);

        taskService.setTaskComplete(2L, 2L, true);

        assertEquals(true, taskRepo.findAllByOwnerId(1L).get(0).isCompleted());
    }

    // expect to raise exception as user with id 1 does not
    // own task with id 2
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(id = 1L)
    public void testSetCompletedWhereWrongTaskIdUsed() throws TaskNotFoundException {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        clientService.createClient("Hat co", "Hat making", "Broadstairs");

        Task t1 = new Task();
        t1.setTaskName("Fix the rocket");
        t1.setClient(clientRepo.findById(1L).get());
        t1.setOwnerId(1L);
        Task t2 = new Task();
        t2.setTaskName("Summarise: what is a hat?");
        t2.setClient(clientRepo.findById(2L).get());
        t2.setOwnerId(2L);

        taskRepo.save(t1);
        taskRepo.save(t2);

        taskService.setTaskComplete(1L, 2L, true);

        assertEquals(true, t2.isCompleted());
    }

}


