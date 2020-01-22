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
        String[] taskNames = {"Build rocket", "Bore some holes", "Nunchucks practice"};

        List<Task> tasks = taskService.getAllTasksByOwnerId(1L);

        // there are two tasks belonging to this user
        assertEquals(3, tasks.size());

        // the tasks are correctly named
        for (int i = 0; i < tasks.size(); i++) {
            assertEquals(taskNames[i], tasks.get(i).getTaskName());
        }
    }

    @Test
    @Transactional
    @WithMockCustomUser( id = 3L )
    public void testAllTasksNoEntries() {
        List<Task> tasks = taskService.getAllTasksByOwnerId(3L);

        // there are no tasks belonging to this user
        assertEquals(0, tasks.size());
    }

    // expect exception where user provides invalid id when searching
    // for tasks
    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser( id = 2L )
    public void testAllTasksTwoEntriesInvalidId() throws ClientNotFoundException {

        taskService.getAllTasksByOwnerId(1L);
    }
}
