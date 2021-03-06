package com.timetracker.timetracker.authenticationcontroller;

import com.timetracker.timetracker.model.CustomPrincipalUser;
import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.security.JwtRequest;
import com.timetracker.timetracker.security.JwtResponse;
import com.timetracker.timetracker.security.JwtTokenUtil;
import com.timetracker.timetracker.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken
            (@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        // find the corresponding user details from the database
        final CustomPrincipalUser userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        // generate the token and append to the response
        final String token = jwtTokenUtil.generateToken(userDetails);
        final Long id = userDetails.getId();
        return ResponseEntity.ok(new JwtResponse(token, id));
    }

    // use AuthenticationManager as a check to confirm valid log in
    // details. This is done by comparing credentials to those stored
    // in the database
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID CREDENTIALS", e);
        }
    }

    // register a new user to the database
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserDetailsResponse> saveUser(@RequestBody User user) throws DuplicateUserException {

        User savedUser;
        try {
            savedUser = userDetailsService.save(user);
        } catch (DataIntegrityViolationException ex) {
            // throw error if user with this email already exists
            throw new DuplicateUserException(user.getEmail());
        }

        return ResponseEntity.ok(new UserDetailsResponse(savedUser));
    }
}