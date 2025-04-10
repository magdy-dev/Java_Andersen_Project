package com.andersen.service.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * CustomPasswordEncoder is a password encoder that uses the SHA-256 hashing algorithm
 * to encode passwords and verifies them by comparing the hashed values.
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    /**
     * Encodes the provided raw password using SHA-256 algorithm and returns it as a Base64 encoded string.
     *
     * @param rawPassword The raw password provided by the user.
     * @return The Base64 encoded string of the hashed password.
     * @throws IllegalStateException if the SHA-256 algorithm is not available.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.toString().getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available");
        }
    }

    /**
     * Checks if the raw password matches the encoded password.
     *
     * @param rawPassword     The raw password to check.
     * @param encodedPassword The previously encoded password to compare against.
     * @return true if the raw password matches the encoded password, false otherwise.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}