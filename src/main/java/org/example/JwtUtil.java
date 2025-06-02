package org.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JWT operations such as token generation, validation, and extraction of claims.
 */
@Service
public class JwtUtil {
    private static final String SECRET_KEY = "secret";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * Extracts the username from the JWT token.
     * @param token the JWT token from which to extract the username
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration date extracted from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a provided claims resolver function.
     * @param token the JWT token from which to extract the claim
     * @param claimsResolver the function to resolve the claim from the Claims object
     * @return the extracted claim value
     * @param <T> the type of the claim to be extracted
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * @param token the JWT token from which to extract all claims
     * @return the Claims object containing all claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(KEY).build()
                .parseSignedClaims(token).getPayload();
    }

    /**
     * Checks if the JWT token is expired.
     * @param token the JWT token to check for expiration
     * @return true if the token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the given user details.
     * @param userDetails the user details for which to generate the token
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     * @param claims the claims to include in the token
     * @param subject the subject (username) for which the token is created
     * @return the generated JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(KEY)
                .compact();
    }

    /**
     * Validates the JWT token against the provided user details.
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
