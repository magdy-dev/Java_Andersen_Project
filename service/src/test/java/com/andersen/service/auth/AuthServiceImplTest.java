package com.andersen.service.auth;

import com.andersen.domain.dto.userrole.AuthResponseDto;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.user.UserRepository;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.service.security.CustomPasswordEncoder;
import com.andersen.service.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private UserRepository userRepository;
    private CustomPasswordEncoder customPasswordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        customPasswordEncoder = mock(CustomPasswordEncoder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);

        authService = new AuthServiceImpl(jwtTokenProvider, customPasswordEncoder, userRepository);
    }

    @Test
    void login_ShouldSucceed_WhenCredentialsAreValid() throws AuthenticationException, DataAccessException {
        String username = "user1";
        String password = "password";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(customPasswordEncoder.encode(password));
        mockUser.setId(1L);

        when(userRepository.getByUsername(username)).thenReturn(mockUser);
        when(customPasswordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(username, mockUser.getRole())).thenReturn("token");

        AuthResponseDto result = authService.login(username, password);

        assertEquals(mockUser.getId(), result.getId());
        assertEquals(mockUser.getUsername(), result.getUsername());
        assertEquals("token", result.getToken());
    }

    @Test
    void login_ShouldFail_WhenCredentialsAreInvalid() {
        String username = "user1";
        String password = "wrongpass";

        when(userRepository.getByUsername(username)).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> authService.login(username, password));
    }

    @Test
    void registerCustomer_ShouldSucceed_WhenValidInput() throws RegistrationException, DataAccessException, AuthenticationException {
        String username = "newUser";
        String password = "securePass123";
        String email = "email@example.com";
        String fullName = "Test User";

        when(userRepository.getByUsername(username)).thenReturn(null);
        when(customPasswordEncoder.encode(password)).thenReturn("encodedPass");

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword("encodedPass");
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setRole(UserRole.CUSTOMER);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = authService.registerCustomer(username, password, email, fullName);

        assertEquals(username, result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerCustomer_ShouldThrow_WhenUsernameExists() {
        String username = "existingUser";
        when(userRepository.getByUsername(username)).thenReturn(new User());

        assertThrows(RegistrationException.class, () ->
                authService.registerCustomer(username, "pass12345", "email@example.com", "Full Name"));
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() throws DataAccessException {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = authService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findById_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DataAccessException.class, () -> authService.findById(99L));
    }
}