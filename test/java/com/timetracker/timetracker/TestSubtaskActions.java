package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.exception.ClientNotFoundException;
import com.timetracker.timetracker.service.exception.TaskNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Checks that actions relating to modifying and retrieving
 * tasks can only be performed by owner
 */

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestSubtaskActions {

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
    SubtaskRepository subtaskRepo;

    // expect successful addition of task where user's id matches task
    // owner id
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateTaskValidId() throws ClientNotFoundException {
        String taskName = "Fix the rocket";
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, taskName, 1L);

        assertEquals(taskService.getAllTasksByOwnerId(1L).get(0).getTaskName(), taskName);
    }



    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateTaskInvalidId() throws TaskNotFoundException {
        clientService.createClient("Tesla", "Space stuff", "Mars");

        Task t = new Task();
        t.setTaskName("Pick moon rocks");
        t.setOwnerId(1L);
        t.setClient(clientRepo.findById(1L).get());
        taskRepo.save(t);

        subtaskService.createSubtask(2L, 1L,"Get borer", "Admin", new ArrayList<>());
    }


    // test to check onwer Task's subtasks are updated to reflect new
    // additions
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testSubtasksSetValidId() throws ClientNotFoundException, TaskNotFoundException {
        clientService.createClient("Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());
        subtaskService.createSubtask(1L, 1L, "Dig stuff", "Labouring", new ArrayList<>());

        Subtask st1 = subtaskRepo.findById(1L).get();
        Subtask st2 = subtaskRepo.findById(2L).get();

        Task t1 = taskRepo.findAllByOwnerId(1L).get(0);
        System.out.println("END UP WITH: " + t1.getSubtasks());

        assertEquals(2, t1.getSubtasks().size());
    }

}


