package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Manages user sessions and authentication tokens in a thread-safe manner.
 * This class maintains active user sessions and provides methods to validate,
 * create, and invalidate sessions. It uses concurrent maps to ensure thread safety
 * when handling multiple simultaneous sessions.
 */
public class SessionManager {
    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, String> userTokens = new ConcurrentHashMap<>();

    /**
     * Creates a new session for the specified user and generates a unique token.
     *
     * @param user the user to create a session for
     * @return the newly generated session token
     * @throws IllegalArgumentException if the user parameter is null
     */
    public String createSession(User user) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, user);
        userTokens.put(user.getUsername(), token);
        return token;
    }
    /**
     * Invalidates the session associated with the given token.
     * Removes both the token-based session and the user-token mapping.
     *
     * @param token the session token to invalidate
     */
    public void invalidateSession(String token) {
        User user = activeSessions.get(token);
        if (user != null) {
            activeSessions.remove(token);
            userTokens.remove(user.getUsername());
        }
    }
    /**
     * Invalidates the session associated with the given token.
     * Removes both the token-based session and the user-token mapping.
     *
     * @param token the session token to invalidate
     */
    public User getUser(String token) {
        return activeSessions.get(token);
    }
    /**
     * Checks if a session token is valid (exists in active sessions).
     *
     * @param token the session token to validate
     * @return true if the token corresponds to an active session, false otherwise
     */

    public boolean isValidSession(String token) {
        return activeSessions.containsKey(token);
    }
    /**
     * Checks if the user associated with the given token has ADMIN role.
     *
     * @param token the session token to check
     * @return true if the session is valid and the user is an ADMIN, false otherwise
     */
    public boolean isAdmin(String token) {
        User user = getUser(token);
        return user != null && user.getRole() == UserRole.ADMIN;
    }
}