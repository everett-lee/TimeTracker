package com.timetracker.timetracker;


import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.repository.TimeCommitRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
import com.timetracker.timetracker.service.TaskService;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import com.timetracker.timetracker.service.TimeCommitService;
import com.timetracker.timetracker.service.exceptions.TimeCommitNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestTimeCommitActions {

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

    // expect successful creation of time commit
    @Test
    @Transactional
    @WithMockCustomUser(id = 1L)
    public void testCreateTimeCommit() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L, "Get borer", "Admin", new ArrayList<>());
        timeCommitService.createTimeCommit(1L, 1L);

        assertEquals(1, subtaskRepo.findById(1L).get().getTimeCommits().size());
    }

    // expect single time commit for duplicate date
    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser(id = 1L)
    public void testCreateTimeCommitDuplicate() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L, "Get borer", "Admin", new ArrayList<>());
        timeCommitService.createTimeCommit(1L, 1L);
        timeCommitService.createTimeCommit(1L, 1L);

        assertEquals(1, subtaskRepo.findById(1L).get().getTimeCommits().size());
    }

    // expect successful creation fails with invalid owner id
    @Test
    @Transactional
    @WithMockCustomUser(id = 2L)
    public void testCreateTimeCommitinvalidId() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        subtaskRepo = mock(SubtaskRepository.class);
        Subtask submock = mock(Subtask.class);
        when(submock.getOwnerId()).thenReturn(1L);
        when(subtaskRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(submock));

        timeCommitService.createTimeCommit(1L, 1L);

    }

    // expect successful deletion
    @Test
    @WithMockCustomUser(id = 1L)
    public void testTimeCommitDeleted() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException, TimeCommitNotFoundException {
        clientService.createClient(1L, "Tesla", "Space stuff", "Mars");
        taskService.createTask(1L, "Bore holes", 1L);

        subtaskService.createSubtask(1L, 1L, "Get borer", "Admin", new ArrayList<>());
        timeCommitService.createTimeCommit(1L, 1L);

        // the time commit was added
        assertEquals(1, timeCommitRepo.count());
        assertEquals(1, subtaskRepo.findById(1L).get().getTimeCommits().size());

        timeCommitService.deleteTimeCommit(1L, 1L);

        // it was deleted
        assertEquals(0, timeCommitRepo.count());
        assertEquals(0, subtaskRepo.findById(1L).get().getTimeCommits().size());
    }

    // expect deletion fails where invalid id provided
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(id = 2L)
    public void testTimeCommitDeletedFailsInvalidId() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException, TimeCommitNotFoundException {
        subtaskRepo.save(new Subtask());

        TimeCommit tc = new TimeCommit();
        tc.setOwnerId(1L);

        timeCommitRepo.save(tc);

        // the time commit was added
        assertEquals(1, timeCommitRepo.count());

        timeCommitService.deleteTimeCommit(1L, 1L);
    }
}