package com.timetracker.timetracker;

import com.timetracker.timetracker.model.Client;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
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
public class TestClientActions {

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

    // expect successful creation of client
    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateClient() {
        String name = "Thom Yorke";
        clientService.createClient(1L, name, "Soundtracks", "UK");

        assertEquals(name, clientRepo.findById(1L).get().getClientName());
    }


    // expect error where wrong id provided
    @Test(expected = AccessDeniedException.class)
    @Transactional
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

        assertEquals(1, clientRepo.count());
        clientService.deleteClient(1L, 1L);
        assertEquals(0, clientRepo.count());
    }

    // expect deletion to fail due to wrong owner id
    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser( id = 2L )
    public void testDeleteClientInvalidId() throws ClientNotFoundException {
        Client c1 = new Client();
        c1.setOwnerId(1L);
        c1.setLocation("");
        c1.setBusinessType("");
        c1.setClientName("");

        clientService.deleteClient(1L, 1L);
    }

    // expect deletion to fail due to task -> client relationship
    @Test (expected =  DataIntegrityViolationException.class)
    @WithMockCustomUser( id = 1L )
    public void testDeleteClientWithRelationship() throws ClientNotFoundException {
        String name = "Thom Yorke";
        clientService.createClient(1L, name, "Soundtracks", "UK");
        taskService.createTask(1L, "", 1L);

        clientService.deleteClient(1L, 1L);
    }

    @Test
    @Transactional
    @WithMockCustomUser( id = 1L )
    public void testCreateGetAllByOwnerId() {
        Client c1 = new Client();
        c1.setOwnerId(1L);
        c1.setLocation("");
        c1.setBusinessType("");
        c1.setClientName("");
        Client c2 = new Client();
        c2.setOwnerId(1L);
        c2.setLocation("");
        c2.setBusinessType("");
        c2.setClientName("");
        Client c3 = new Client();
        c3.setOwnerId(2L);
        c3.setLocation("");
        c3.setBusinessType("");
        c3.setClientName("");

        clientRepo.save(c1);
        clientRepo.save(c2);

        int n = clientService.getAllClientsByOwnerId(1L).size();

        // there are the correct number of clients saved
        assertEquals(2, n);
    }

    // expect error where wrong id used for look up
    @Test(expected = AccessDeniedException.class)
    @Transactional
    @WithMockCustomUser( id = 2L )
    public void testCreateGetAllByOwnerIdInvalidId() {
        Client c1 = new Client();
        c1.setOwnerId(1L);
        c1.setLocation("");
        c1.setBusinessType("");
        c1.setClientName("");
        Client c2 = new Client();
        c2.setOwnerId(1L);
        c2.setLocation("");
        c2.setBusinessType("");
        c2.setClientName("");
        Client c3 = new Client();
        c3.setOwnerId(2L);
        c3.setLocation("");
        c3.setBusinessType("");
        c3.setClientName("");

        clientRepo.save(c1);
        clientRepo.save(c2);

        int n = clientService.getAllClientsByOwnerId(1L).size();
    }

}


