package com.andersen.ui.globalException;

import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 *
 * This class handles exceptions thrown throughout the application,
 * providing custom responses for specific exception types.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles AuthenticationException and returns a 401 Unauthorized response.
     *
     * @param ex the AuthenticationException thrown
     * @return ResponseEntity containing the error details
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthException(AuthenticationException ex) {
        logger.warn("Authentication error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Handles RegistrationException and returns a 400 Bad Request response.
     *
     * @param ex the RegistrationException thrown
     * @return ResponseEntity containing the error details
     */
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        logger.warn("Registration error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles DataAccessException and returns a 500 Internal Server Error response.
     *
     * @param ex the DataAccessException thrown
     * @return ResponseEntity containing the error details
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        logger.error("Database access error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handles any other unhandled exceptions and returns a 500 Internal Server Error response.
     *
     * @param ex the Exception thrown
     * @return ResponseEntity containing a generic error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Unhandled exception caught", ex);

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorBody.put("error", "Internal Server Error");
        errorBody.put("message", "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    /**
     * Builds a structured response entity for error handling.
     *
     * @param status  the HTTP status to return
     * @param message the error message to include in the response
     * @return ResponseEntity containing the structured error response
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}