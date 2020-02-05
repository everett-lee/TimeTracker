package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.Task;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.repository.TimeCommitRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.TimeCommitService;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.DeletedDependencyException;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    TimeCommitService timeCommitService;
    @Autowired
    TaskRepository taskRepo;
    @Autowired
    ClientRepository clientRepo;
    @Autowired
    SubtaskRepository subtaskRepo;
    @Autowired
    TimeCommitRepository timeCommitRepo;

    // expect successful addition of subtask where user's id matches task
    // owner id
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateSubtask() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        String subtaskOneName = "Gardening";
        String subtaskTwoName = "Do some jiu jits";
        subtaskService.createSubtask(1L, 3L, subtaskOneName, "Botany"
                                     , new ArrayList<>());
        subtaskService.createSubtask(1L, 3L, subtaskTwoName, "Sports"
                , new ArrayList<>(Arrays.asList(1L)));

        Task t = taskService.getAllTasksByOwnerId(1L).get(2);

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
    @WithMockCustomUser( id = 1L )
    public void testCreateSubtaskInvalidTaskId() throws TaskNotFoundException, SubtaskNotFoundException {
        Task t = new Task();
        t.setTaskName("Pick moon rocks");
        t.setOwnerId(2L);
        taskRepo.save(t);

        subtaskService.createSubtask(1L, 4L,"Get borer", "Admin", new ArrayList<>());
    }

    // expect subtask to be deleted
    @Test
    @WithMockCustomUser( id = 1L )
    public void testDeleteSubtask() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException, DeletedDependencyException {
        // there is one subtask of the  task
        assertEquals(2, taskRepo.findById(1L).get().getSubtasks().size());

        subtaskService.deleteSubtask(1L, 2L);

        // there is one subtask
        assertEquals(1, taskRepo.findById(1L).get().getSubtasks().size());
    }

    // expect deletion to fail due to wrong id
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 2L )
    public void testDeleteSubtaskWrongId() throws DeletedDependencyException, SubtaskNotFoundException {
        Subtask subtask = new Subtask();
        subtask.setOwnerId(3L);
        subtaskRepo.save(subtask);

        subtaskService.deleteSubtask(3L, 1L);
    }


    // expect subtask deletion to fail if it is a dependency
    @Test(expected = DeletedDependencyException.class)
    @WithMockCustomUser( id = 1L )
    @Transactional
    public void testDeleteSubtaskDependencyMaintained() throws SubtaskNotFoundException, TaskNotFoundException, DeletedDependencyException {
        // there are two subtasks
        assertEquals(2, subtaskRepo.count());
        assertEquals(2, taskRepo.findById(1L).get().getSubtasks().size());

        subtaskService.deleteSubtask(1L, 1L);
    }


    // test to check owner Task's subtasks are updated to reflect new additions
    @Test
    @WithMockCustomUser( id = 1L )
    public void testSubtasksSetValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        subtaskService.createSubtask(1L, 1L,"Buy snakes", "Admin", new ArrayList<>());

        Task t1 = taskRepo.findAllByOwnerId(1L).get(0);

        assertEquals(3, t1.getSubtasks().size());
    }

    // check that subtask set as complete
    @Test
    @WithMockCustomUser( id = 1L )
    public void testSubtaskSetCompleteWithValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        subtaskService.setSubtaskComplete(1L, 1L, true);

        boolean subTaskCompleted  = subtaskRepo.findById(1L).get().isCompleted();
        LocalDate dateCompleted = subtaskRepo.findById(1L).get().getDateCompleted();

        assertEquals(true, subTaskCompleted);
        assertEquals(LocalDate.now(), dateCompleted);
    }

    // check that subtask set as not complete after two changes
    @Test
    @WithMockCustomUser( id = 1L )
    public void testSubtaskNotCompletedWithValidId() throws ClientNotFoundException, TaskNotFoundException, SubtaskNotFoundException {
        subtaskService.setSubtaskComplete(1L, 1L, true);
        subtaskService.setSubtaskComplete(1L, 1L, false);

        boolean subTaskCompleted  = subtaskRepo.findById(1L).get().isCompleted();
        LocalDate dateCompleted = subtaskRepo.findById(1L).get().getDateCompleted();

        assertEquals(false, subTaskCompleted);
        assertEquals(null, dateCompleted);
    }

    // check that subtask time commits are returned
    @Test
    @WithMockCustomUser( id = 1L )
    public void testSubtaskTimeCommitsReturned() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        timeCommitService.createTimeCommit(1L, 1L, 0L);

        List<TimeCommit> timeCommits = subtaskService.timeCommits(subtaskRepo.findById(1L).get());

        assertEquals(1, timeCommits.size());
    }


    // check that subtask time commits raises exception for
    // invalid id
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 2L )
    public void testSubtaskTimeCommitsException() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        subtaskRepo = mock(SubtaskRepository.class);
        when(subtaskRepo.findById(1L)).thenReturn(java.util.Optional.of(new Subtask()));
        List<TimeCommit> timeCommits = subtaskService.timeCommits(subtaskRepo.findById(1L).get());
    }


}


