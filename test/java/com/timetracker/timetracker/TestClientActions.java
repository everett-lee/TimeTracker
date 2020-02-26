package com.timetracker.timetracker;

import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.repository.SubtaskRepository;
import com.timetracker.timetracker.repository.TaskRepository;
import com.timetracker.timetracker.service.ClientService;
import com.timetracker.timetracker.service.SubtaskService;
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

import static org.junit.Assert.assertEquals;

/**
 * Checks that actions relating to modifying and retrieving
 * subtasks are performed correctly
 */

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Transactional
public class TestClientActions {
    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepo;

    // expect successful creation of client
    @Test
    @WithMockCustomUser( id = 1L )
    public void testCreateClient() {
        String name = "Thom Yorke";
        clientService.createClient(1L, name, "Soundtracks", "UK");

        assertEquals(name, clientRepo.findById(3L).get().getClientName());
    }


    // expect error where wrong id provided
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 2L )
    public void testCreateClientinvalidId() {
        String name = "Thom Yorke";
        clientService.createClient(1L, name, "Soundtracks", "UK");

        assertEquals(name, clientRepo.findById(1L).get().getClientName());
    }

    // expect client to be deleted
    @Test
    @WithMockCustomUser( id = 1L )
    public void testDeleteClient() throws ClientNotFoundException {
        String name = "Thom Yorke";
        clientService.createClient(1L, name, "Soundtracks", "UK");

        assertEquals(3, clientRepo.count());
        clientService.deleteClient(1L, 3L);
        assertEquals(2, clientRepo.count());
    }

    // expect deletion to fail due to wrong owner id
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 2L )
    public void testDeleteClientInvalidId() throws ClientNotFoundException {
        clientService.deleteClient(1L, 1L);
    }

    @Test
    @WithMockCustomUser( id = 1L )
    public void testCreateGetAllByOwnerId() {
        int n = clientService.getAllClientsByOwnerId(1L).size();

        // there are the correct number of clients saved
        assertEquals(2, n);
    }

    // expect error where wrong id used for look up
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 2L )
    public void testCreateGetAllByOwnerIdInvalidId() {

        int n = clientService.getAllClientsByOwnerId(1L).size();
    }

}


