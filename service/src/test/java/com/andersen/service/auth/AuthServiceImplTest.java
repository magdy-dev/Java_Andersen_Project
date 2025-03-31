package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;

import com.andersen.exception.DataAccessException;
import com.andersen.logger.logger.Out_put_Logger;


import com.andersen.repository_JPA.user.UserRepository;
import com.andersen.service.Security.PasswordEncoder;
import com.andersen.service.Security.SessionManager;
import com.andersen.service.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    private UserRepository userRepository;
    private SessionManager sessionManager;
    private AuthServiceImpl authService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        sessionManager = Mockito.mock(SessionManager.class);
        authService = new AuthServiceImpl(userRepository, sessionManager,passwordEncoder);
    }

    @Test
    public void testLogin_Success() throws Exception, AuthenticationException {
        String username = "testUser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(authService.hashPassword(password)); // Assuming this method is accessible
        user.setRole(UserRole.CUSTOMER);

        when(userRepository.getUserByUsername(username));

        User loggedInUser = authService.login(username, password);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        verify(sessionManager, times(1)).createSession(user);
        Out_put_Logger.log("Login test passed.");
    }

    @Test
    public void testLogin_InvalidUsername() throws DataAccessException {
        String username = "invalidUser";
        String password = "password";

        when(userRepository.getUserByUsername(username));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.login(username, password);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        verify(sessionManager, never()).createSession(any());
    }

    @Test
    public void testLogin_InvalidPassword() throws Exception, AuthenticationException {
        String username = "testUser";
        String password = "wrongPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(authService.hashPassword("password")); // Correct password

        when(userRepository.getUserByUsername(username));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.login(username, password);
        });

        assertEquals("Invalid username or password", exception.getMessage());
        verify(sessionManager, never()).createSession(any());
    }

    @Test
    public void testRegisterCustomer_Success() throws Exception, AuthenticationException {
        String username = "newUser";
        String password = "password";
        String email = "newuser@example.com";
        String fullName = "New User";

        when(userRepository.getUserByUsername(username));

        // Mocking the behavior of userRepository for creating a user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(authService.hashPassword(password));
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setRole(UserRole.CUSTOMER);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Call the registerCustomer method
        User registeredUser = authService.registerCustomer(username, password, email, fullName);

        // Assertions to verify the expected outcomes
        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(email, registeredUser.getEmail());
        assertEquals(fullName, registeredUser.getFullName());
        assertEquals(UserRole.CUSTOMER, registeredUser.getRole());

        // Verify that the repository methods were called as expected
        verify(userRepository, times(1)).getUserByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
        Out_put_Logger.log("Register customer test passed.");
    }}

// Additional tests could be added here to cover more scenarios, such as registration failures
