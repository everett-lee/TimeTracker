package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Subtask;
import com.timetracker.timetracker.model.TimeCommit;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TimeCommitRepository;
import com.timetracker.timetracker.service.TimeCommitService;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import com.timetracker.timetracker.service.exceptions.SubtaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TaskNotFoundException;
import com.timetracker.timetracker.service.exceptions.TimeCommitNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestTimeCommitActions {

    @Autowired
    TimeCommitService timeCommitService;
    @Autowired
    SubtaskRepository subtaskRepo;
    @Autowired
    TimeCommitRepository timeCommitRepo;

    // expect successful creation of time commit
    @Test
    @WithMockCustomUser(id = 1L)
    public void testCreateTimeCommit() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        timeCommitService.createOrUpdateTimeCommit(1L, 1L, 50L);

        assertEquals(3, subtaskRepo.findById(1L).get().getTimeCommits().size());
    }

    @Test
    @WithMockCustomUser(id = 1L)
    public void testCreateThenUpdateTimeCommit() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        timeCommitService.createOrUpdateTimeCommit(1L, 1L, 42L);

        assertEquals(3, subtaskRepo.findById(1L).get().getTimeCommits().size());

        TimeCommit timeCommit = subtaskRepo.findById(1L).get().getTimeCommits().get(2);
        assertEquals(42L, timeCommit.getTime().longValue());


        // Time commit exists for same date so additional time will be added
        timeCommitService.createOrUpdateTimeCommit(1L, 1L, 38L);
        timeCommit = subtaskRepo.findById(1L).get().getTimeCommits().get(2);
        assertEquals(80L, timeCommit.getTime().longValue());
    }

    // expect single time commit for duplicate date
    @Test
    @WithMockCustomUser(id = 1L)
    public void testCreateTimeCommitDuplicate() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        timeCommitService.createOrUpdateTimeCommit(1L, 1L, 0L);
        timeCommitService.createOrUpdateTimeCommit(1L, 1L, 0L);

        assertEquals(3, subtaskRepo.findById(1L).get().getTimeCommits().size());
    }

    // expect successful creation fails with invalid owner id
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(id = 2L)
    public void testCreateTimeCommitinvalidId() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException {
        subtaskRepo = mock(SubtaskRepository.class);
        Subtask submock = mock(Subtask.class);
        when(submock.getOwnerId()).thenReturn(1L);
        when(subtaskRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(submock));

        timeCommitService.createOrUpdateTimeCommit(1L, 1L, 0L);
    }

    // expect successful deletion
    @Test
    @WithMockCustomUser(id = 1L)
    public void testTimeCommitDeleted() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException, TimeCommitNotFoundException {
        // two time commits exist for this subtask
        assertEquals(2, subtaskRepo.findById(1L).get().getTimeCommits().size());

        timeCommitService.deleteTimeCommit(1L, 1L, 1L);

        assertEquals(1, subtaskRepo.findById(1L).get().getTimeCommits().size());
    }

    // expect deletion fails where invalid id provided
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(id = 2L)
    public void testTimeCommitDeletedFailsInvalidId() throws SubtaskNotFoundException, TimeCommitNotFoundException {
        timeCommitService.deleteTimeCommit(1L, 1L, 1L);
    }

    // expect successful update of time
    @Test
    @WithMockCustomUser(id = 1L)
    public void testUpdateTime() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException, TimeCommitNotFoundException {
        TimeCommit timeCommit = timeCommitRepo.findById(1L).get();

        assertEquals(Long.valueOf(22), timeCommit.getTime());
        timeCommitService.updateTimeCommit(1L, 1L, 5L);

        timeCommit = timeCommitRepo.findById(1L).get();
        assertEquals(Long.valueOf(5), timeCommit.getTime());
    }

    // expect successful retrieval of timecommits
    @Test
    @WithMockCustomUser(id = 1L)
    public void testGetTimeCommits() throws ClientNotFoundException, SubtaskNotFoundException, TaskNotFoundException, TimeCommitNotFoundException {
        List<TimeCommit> timeCommits = timeCommitService.getAllTimeCommitsByOwnerAndSubtaskIds(1L, 1L);

        assertEquals(2, timeCommits.size());
    }
}