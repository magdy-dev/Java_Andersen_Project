package com.andersen.service.Security;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
/**
 * A component for encoding and verifying passwords.
 * This class provides methods to encode a raw password using the SHA-256 hashing algorithm
 * and to verify if a raw password matches an encoded password.
 */
@Component
public class PasswordEncoder {

    /**
     * Encodes a raw password using SHA-256 hashing algorithm.
     *
     * @param rawPassword the raw password to encode
     * @return the Base64 encoded representation of the hashed password
     * @throws IllegalStateException if the SHA-256 algorithm is not available
     */
    public String encode(CharSequence rawPassword) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Hash the raw password
            byte[] hash = digest.digest(rawPassword.toString().getBytes());
            // Encode the hash to Base64 and return
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available");
        }
    }

    /**
     * Verifies if the raw password matches the encoded password.
     *
     * @param rawPassword    the raw password to check
     * @param encodedPassword the previously encoded password to compare against
     * @return true if the raw password matches the encoded password, false otherwise
     */
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}