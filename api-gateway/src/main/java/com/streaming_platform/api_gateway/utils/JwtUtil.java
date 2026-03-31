package com.streaming_platform.api_gateway.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

/**
 * Utility class for handling JWT operations such as validation and claim extraction.
 *
 * <p>This class is used at the API Gateway level to:
 * <ul>
 *     <li>Validate incoming JWT tokens</li>
 *     <li>Extract user identity from token</li>
 *     <li>Ensure token integrity and expiration</li>
 * </ul>
 *
 * <p><b>Security Notes:</b>
 * <ul>
 *     <li>Secret key must be Base64 encoded and at least 256 bits long</li>
 *     <li>Token parsing validates signature and expiration</li>
 * </ul>
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    /**
     * Initializes the signing key once during bean creation.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts user ID (subject) from JWT token.
     *
     * @param token JWT token (without "Bearer " prefix)
     * @return user ID stored in token subject
     * @throws JwtException if token is invalid
     */
    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Validates the JWT token.
     *
     * @param token JWT token (without "Bearer " prefix)
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // Token expired
            log("JWT expired", ex);
        } catch (UnsupportedJwtException ex) {
            log("JWT unsupported", ex);
        } catch (MalformedJwtException ex) {
            log("JWT malformed", ex);
        }
         catch (IllegalArgumentException ex) {
            log("JWT claims string is empty", ex);
        }
        return false;
    }

    /**
     * Parses JWT and extracts claims.
     *
     * @param token JWT token
     * @return claims contained in token
     * @throws JwtException if parsing fails
     */
    public Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Basic logging method.
     */
    private void log(String message, Exception ex) {
        log.error("{}:{}", message, ex);
    }
}