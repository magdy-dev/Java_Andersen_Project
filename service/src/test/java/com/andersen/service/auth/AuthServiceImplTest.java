package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.exception.DataAccessException;
import com.andersen.repository.user.UserRepository;
import com.andersen.service.excption.AuthenticationException;
import com.andersen.service.excption.RegistrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private final String testUsername = "testuser";
    private final String testPassword = "password123";
    private final String testEmail = "test@example.com";
    private final String testFullName = "Test User";

    @BeforeEach
    void setUp() throws AuthenticationException {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(testUsername);
        testUser.setPassword(authService.hashPassword(testPassword));
        testUser.setEmail(testEmail);
        testUser.setFullName(testFullName);
        testUser.setRole(UserRole.CUSTOMER);
    }

    @Test
    void login_Successful() throws AuthenticationException, DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));

        User loggedInUser = authService.login(testUsername, testPassword);

        assertNotNull(loggedInUser);
        assertEquals(testUsername, loggedInUser.getUsername());
        verify(userRepository, times(1)).getUserByUsername(testUsername);
    }

    @Test
    void login_InvalidUsername_ThrowsException() throws DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () ->
                authService.login(testUsername, testPassword));
    }

    @Test
    void login_InvalidPassword_ThrowsException() throws DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));

        assertThrows(AuthenticationException.class, () ->
                authService.login(testUsername, "wrongpassword"));
    }

    @Test
    void login_RepositoryError_ThrowsException() throws DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenThrow(new DataAccessException("DB error"));

        assertThrows(AuthenticationException.class, () ->
                authService.login(testUsername, testPassword));
    }

    @Test
    void registerCustomer_Successful() throws RegistrationException, DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.empty());
        when(userRepository.createUser(any(User.class))).thenReturn(testUser);

        User registeredUser = authService.registerCustomer(testUsername, testPassword, testEmail, testFullName);

        assertNotNull(registeredUser);
        assertEquals(testUsername, registeredUser.getUsername());
        verify(userRepository, times(1)).getUserByUsername(testUsername);
        verify(userRepository, times(1)).createUser(any(User.class));
    }

    @Test
    void registerCustomer_UsernameExists_ThrowsException() throws DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));

        assertThrows(RegistrationException.class, () ->
                authService.registerCustomer(testUsername, testPassword, testEmail, testFullName));
    }

    @Test
    void registerCustomer_RepositoryError_ThrowsException() throws DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.empty());
        when(userRepository.createUser(any(User.class))).thenThrow(new DataAccessException("DB error"));

        assertThrows(RegistrationException.class, () ->
                authService.registerCustomer(testUsername, testPassword, testEmail, testFullName));
    }

    @Test
    void logout_UserLoggedIn_ClearsCurrentUser() throws AuthenticationException, DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));
        authService.login(testUsername, testPassword);

        assertNotNull(authService.getCurrentUser());
        authService.logout();

        assertNull(authService.getCurrentUser());
    }

    @Test
    void logout_NoUserLoggedIn_DoesNothing() {
        assertNull(authService.getCurrentUser());
        authService.logout();
        assertNull(authService.getCurrentUser());
    }

    @Test
    void getCurrentUser_ReturnsLoggedInUser() throws AuthenticationException, DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));
        authService.login(testUsername, testPassword);

        User currentUser = authService.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals(testUsername, currentUser.getUsername());
    }

    @Test
    void getCurrentUser_NoUserLoggedIn_ReturnsNull() {
        assertNull(authService.getCurrentUser());
    }

    @Test
    void isAdmin_AdminUser_ReturnsTrue() throws AuthenticationException, DataAccessException {
        testUser.setRole(UserRole.ADMIN);
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));
        authService.login(testUsername, testPassword);

        assertTrue(authService.isAdmin());
    }

    @Test
    void isAdmin_CustomerUser_ReturnsFalse() throws AuthenticationException, DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));
        authService.login(testUsername, testPassword);

        assertFalse(authService.isAdmin());
    }

    @Test
    void isAdmin_NoUserLoggedIn_ReturnsFalse() {
        assertFalse(authService.isAdmin());
    }

    @Test
    void isAuthenticated_UserLoggedIn_ReturnsTrue() throws AuthenticationException, DataAccessException {
        when(userRepository.getUserByUsername(testUsername)).thenReturn(Optional.of(testUser));
        authService.login(testUsername, testPassword);

        assertTrue(authService.isAuthenticated());
    }

    @Test
    void isAuthenticated_NoUserLoggedIn_ReturnsFalse() {
        assertFalse(authService.isAuthenticated());
    }

    @Test
    void hashPassword_ValidPassword_ReturnsHashedValue() throws AuthenticationException {
        String password = "testpassword";
        String hashedPassword = authService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }
}