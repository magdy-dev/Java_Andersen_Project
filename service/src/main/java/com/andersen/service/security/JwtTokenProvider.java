package com.andersen.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.andersen.domain.entity.role.UserRole;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Component for managing JSON Web Token (JWT) operations.
 * This class provides methods for creating, parsing, and validating JWTs.
 * It utilizes HMAC-SHA512 for signing tokens and requires a base64-encoded secret key.
 *
 * <p>The class supports the following functionalities:
 * <ul>
 *     <li>Creating JWT tokens with claims for username and user role.</li>
 *     <li>Extracting claims such as username and role from tokens.</li>
 *     <li>Validating token expiration and integrity.</li>
 * </ul>
 *
 * <p>Usage of this component requires the configuration of JWT secret and expiration time
 * in the application properties.
 *
 * @see UserRole
 */
@Component
public class JwtTokenProvider {

    // Expected to be a Base64-encoded string that decodes to at least 32 bytes
    @Value("${app.jwt.secret:}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationInMs;

    private Key key;

    /**
     * Initializes the JWT key by decoding the provided secret or generating a new key.
     * This method is called after the bean is constructed.
     *
     * @throws IllegalArgumentException if the provided secret cannot be decoded.
     */
    @PostConstruct
    public void init() {
        try {
            // Check if jwtSecret is provided and decodes to at least 32 bytes
            if (jwtSecret != null && !jwtSecret.trim().isEmpty()) {
                byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
                if (decodedKey.length >= 32) {
                    key = Keys.hmacShaKeyFor(decodedKey);
                    return;
                }
            }
        } catch (IllegalArgumentException e) {
            // If decoding fails, we will generate a key below.
        }
        // Fallback: Generate a secure key (256 bits) if not provided or invalid.
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println("No valid secret provided; generated a secure key.");
    }

    /**
     * Creates a JWT token containing the username and user role as claims.
     *
     * @param username the username for which the token is generated.
     * @param userRole the role of the user.
     * @return a signed JWT token as a String.
     */
    public String createToken(String username, UserRole userRole) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", userRole.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token.
     * @return the username contained in the token.
     */
    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts the user role from the JWT token.
     *
     * @param token the JWT token.
     * @return the role as a String.
     */
    public String getRole(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    /**
     * Validates the JWT token.
     *
     * @param token the JWT token.
     * @return true if valid, false otherwise.
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT token.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token.
     * @return the expiration date as a Date object.
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Retrieves a specific claim from the JWT token.
     *
     * @param token the JWT token.
     * @param claimsResolver a functional interface for resolving claims.
     * @param <T> the type of the claim to be resolved.
     * @return the resolved claim.
     */
    private <T> T getClaimFromToken(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.resolve(claims);
    }

    /**
     * Retrieves all claims from the JWT token.
     *
     * @param token the JWT token.
     * @return the claims contained in the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Functional interface for resolving claims from a Claims object.
     *
     * @param <T> the type of the claim to be resolved.
     */
    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}