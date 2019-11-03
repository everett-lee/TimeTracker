package com.timetracker.timetracker;

import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.repository.*;
import com.timetracker.timetracker.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestUserActions {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUserDetailsService userDetailsService;

    @Test
    public void testUserCreated() {
        User user = new User();
        user.setEmail("text@example.com");
        user.setPassword("password");
        userDetailsService.save(user);
        user = new User();
        user.setEmail("what@example.com");
        user.setPassword("password");
        userDetailsService.save(user);

        assertEquals(2, userRepository.count());
    }


    // creation of user with duplicate email should raise
    // exception
    @Test(expected = DataIntegrityViolationException.class)
    public void testDupliateUser() {
        String email = "text@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        userDetailsService.save(user);
        user = new User();
        user.setEmail(email);
        user.setPassword("password");
        userDetailsService.save(user);
    }


    // creation of user with null email should raise
    // exception
    @Test(expected = DataIntegrityViolationException.class)
    public void testNullEmail() {
        String email = null;
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        userDetailsService.save(user);
    }
}