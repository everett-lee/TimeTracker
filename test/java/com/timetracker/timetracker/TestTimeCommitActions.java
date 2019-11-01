package com.timetracker.timetracker;


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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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
    @Test
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

    // expect successful deletion
    @Transactional
    @WithMockCustomUser(id = 1L)
    public void testDeleteTimeCommit() {

    }
}