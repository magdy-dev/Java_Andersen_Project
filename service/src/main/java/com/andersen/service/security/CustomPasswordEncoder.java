package com.andersen.service.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * A custom implementation of the Spring {@link PasswordEncoder} interface.
 * This class handles password encoding using the SHA-256 hashing algorithm,
 * and it encodes the hashed password in Base64 format.
 *
 * <p>This implementation provides two main functionalities:
 * <ul>
 *     <li><strong>Encoding:</strong> Converts the raw password into a hashed
 *     representation using SHA-256, and then encodes it in Base64.</li>
 *     <li><strong>Matching:</strong> Compares a raw password with an encoded
 *     password by encoding the raw password and checking equality.</li>
 * </ul>
 *
 * <p>Note: It is important to use a secure hash function and to store the
 * encoded passwords securely to protect user credentials.</p>
 *
 * @see PasswordEncoder
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    /**
     * Encodes the given raw password using SHA-256 and returns the Base64
     * encoded result.
     *
     * @param rawPassword the raw password to encode
     * @return the Base64 encoded SHA-256 hash of the raw password
     * @throws IllegalStateException if the SHA-256 algorithm is not available
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.toString().getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Checks whether the raw password matches the encoded password.
     * This is done by encoding the raw password and comparing it to the
     * encoded password.
     *
     * @param rawPassword     the raw password to check
     * @param encodedPassword the previously encoded password to compare with
     * @return true if the raw password matches the encoded password, false
     * otherwise
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}