package com.timetracker.timetracker;

import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestAddUser {

    @Autowired
    ClientService clientService;

    @Autowired
    TaskService taskService;

    // expect successful addition of task where user's id matches task
    // owner id
    @Test
    @WithMockCustomUser( id = 1L )
    public void testCreateTaskValidId() {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Fix the rocket", 1L);
    }

    // expect access to be denied where user id does not match the task
    // owner id
    @Test (expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 4L )
    public void testCreateTaskInvalidId() {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Fix the rocket", 1L);
    }
}
