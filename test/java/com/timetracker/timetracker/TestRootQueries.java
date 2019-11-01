package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestRootQueries {

    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepo;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepository taskRepo;

    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testAllTasksTwoEntries() throws ClientNotFoundException {
        String[] taskNames = {"Bore holds", "Blast rocks"};

        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, taskNames[0], 1L);
        taskService.createTask(1L, taskNames[1], 1L);

        List<Task> tasks = taskService.getAllTasksByOwnerId(1L);

        // there are two tasks belonging to this user
        assertEquals(2, tasks.size());

        // the tasks are correctly named
        for (int i = 0; i < tasks.size(); i++) {
            assertEquals(taskNames[i], tasks.get(i).getTaskName());
        }
    }

    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testAllTasksNoEntries() {

        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");

        List<Task> tasks = taskService.getAllTasksByOwnerId(1L);

        // there are no tasks belonging to this user
        assertEquals(0, tasks.size());
    }

    // expect exception where user provides invalid id when searching
    // for tasks
    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser( id = 2L )
    public void testAllTasksTwoEntriesInvalidId() throws ClientNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        clientService.createClient(1L, "Hat co", "Hat making", "Broadstairs");

        Task t1 = new Task();
        t1.setTaskName("Fix the rocket");
        t1.setClient(clientRepo.findById(1L).get());
        t1.setOwnerId(1L);
        Task t2 = new Task();
        t2.setTaskName("Summarise: what is a hat?");
        t2.setClient(clientRepo.findById(2L).get());
        t2.setOwnerId(1L);

        taskRepo.save(t1);
        taskRepo.save(t2);

        taskService.getAllTasksByOwnerId(1L);
    }
}
