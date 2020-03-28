package com.timetracker.timetracker;

import com.timetracker.timetracker.model.User;
import com.timetracker.timetracker.repository.UserRepository;
import com.timetracker.timetracker.service.JwtUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Factory for creating mock security context for testing.
 */
public class WithMockCustomUserFactory implements WithSecurityContextFactory<WithMockCustomUser> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(any())).thenReturn(createUser(annotation));

        JwtUserDetailsService jwtUserDetailsService
                = new JwtUserDetailsService(userRepository, new BCryptPasswordEncoder());

        UserDetails principal = jwtUserDetailsService.loadUserByUsername(annotation.email());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication
                (new UsernamePasswordAuthenticationToken(principal,
                        principal.getPassword(), principal.getAuthorities()));

        return context;
    }

    private User createUser(WithMockCustomUser annotation) {
        return new User(annotation.id(), annotation.email());
    }
}
