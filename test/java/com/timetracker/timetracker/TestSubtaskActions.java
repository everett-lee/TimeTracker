package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Checks that actions relating to modifying and retrieving
 * subtasks are performed correctly
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

    // expect successful addition of subtask where user's id matches task
    // owner id
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateSubtaskValidId() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        String taskName = "Fix the rocket";
        String subtaskOneName = "Check the booster";
        String subtaskTwoName = "Buy new rocket fuel";
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, taskName, 1L);
        subtaskService.createSubtask(1L, 1L, subtaskOneName, "Mechanic"
                                     , new ArrayList<>());
        subtaskService.createSubtask(1L, 1L, subtaskTwoName, "Shopping"
                , new ArrayList<>(Arrays.asList(1L)));

        Task t = taskService.getAllTasksByOwnerId(1L).get(0);

        // there are two subtasks
        assertEquals(2, t.getSubtasks().size());
        // first subtask has correct name
        assertEquals(t.getSubtasks().get(0).getSubtaskName(), subtaskOneName);
        // second subtask has correct name
        assertEquals(t.getSubtasks().get(1).getSubtaskName(), subtaskTwoName);
        // first subtask has zero dependencies
        assertEquals(t.getSubtasks().get(0).getDependsOn().size(), 0);
        // second subtask has one dependency
        assertEquals(t.getSubtasks().get(1).getDependsOn().size(), 1);
    }

    // expect exception where subtask owner id does not match user id
    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateSubtaskInvalidId() throws TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");

        Task t = new Task();
        t.setTaskName("Pick moon rocks");
        t.setOwnerId(1L);
        t.setClient(clientRepo.findById(1L).get());
        taskRepo.save(t);

        subtaskService.createSubtask(2L, 1L,"Get borer", "Admin", new ArrayList<>());
    }

    // expect exception where task owner id does not match user id
    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateSubtaskInvalidTasolId() throws TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");

        Task t = new Task();
        t.setTaskName("Pick moon rocks");
        t.setOwnerId(2L);
        t.setClient(clientRepo.findById(1L).get());
        taskRepo.save(t);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());
    }


    // test to check owner Task's subtasks are updated to reflect new
    // additions
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testSubtasksSetValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());
        subtaskService.createSubtask(1L, 1L, "Dig stuff", "Labouring", new ArrayList<>());

        Subtask st1 = subtaskRepo.findById(1L).get();
        Subtask st2 = subtaskRepo.findById(2L).get();

        Task t1 = taskRepo.findAllByOwnerId(1L).get(0);
        System.out.println("END UP WITH: " + t1.getSubtasks());

        assertEquals(2, t1.getSubtasks().size());
    }

    // check that subtask time is updated correctly
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testTimeUpdatedValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());

        subtaskService.setSubtaskTime(1L, 1L, 100L);

        Long subtaskTime = subtaskRepo.findById(1L).get().getTotalTime();

        assertEquals(Long.valueOf(100), subtaskTime);
    }


    // check that subtask time is correct following multiple updates
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testTimeUpdatedTwiceValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());

        subtaskService.setSubtaskTime(1L, 1L, 100L);
        subtaskService.setSubtaskTime(1L, 1L, 0L);

        Long subtaskTime = subtaskRepo.findById(1L).get().getTotalTime();

        assertEquals(Long.valueOf(0), subtaskTime);
    }


    // check time update fails where invalid id is used
    @Test( expected = AccessDeniedException.class )
    @Transactional
    @WithMockCustomUser( id = 5L )
    public void testTimeUpdatedInvalidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());

        subtaskService.setSubtaskTime(1L, 1L, 100L);

        Long subtaskTime = subtaskRepo.findById(1L).get().getTotalTime();

        assertEquals(Long.valueOf(100), subtaskTime);
    }

    // check that subtask set as complete
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testSubtaskSetCompleteWithValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());

        subtaskService.setSubtaskComplete(1L, 1L, true);

        boolean subTaskCompleted  = subtaskRepo.findById(1L).get().isCompleted();
        LocalDate dateCompleted = subtaskRepo.findById(1L).get().getDateCompleted();

        assertEquals(true, subTaskCompleted);
        assertEquals(LocalDate.now(), dateCompleted);
    }

    // check that subtask set as not complete after two changes
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testSubtaskNotCompletedWithValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L,"Get borer", "Admin", new ArrayList<>());

        subtaskService.setSubtaskComplete(1L, 1L, true);
        subtaskService.setSubtaskComplete(1L, 1L, false);

        boolean subTaskCompleted  = subtaskRepo.findById(1L).get().isCompleted();
        LocalDate dateCompleted = subtaskRepo.findById(1L).get().getDateCompleted();

        assertEquals(false, subTaskCompleted);
        assertEquals(null, dateCompleted);
    }


}


