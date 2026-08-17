package com.velinfotech.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/** Issues and validates the HS256 tokens the admin console signs in with. */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    /** HS256 needs at least 256 bits of key material. */
    private static final int MIN_SECRET_BYTES = 32;

    @Value("${jwt.secret:}")
    private String configuredSecret;

    @Value("${jwt.expiration-minutes:720}")
    private long expirationMinutes;

    private SecretKey key;

    @PostConstruct
    void init() {
        byte[] material;

        if (configuredSecret == null || configuredSecret.isBlank()) {
            // Fail safe rather than fail open: a random per-boot key keeps the API
            // locked, at the cost of invalidating tokens on restart.
            material = new byte[MIN_SECRET_BYTES];
            new SecureRandom().nextBytes(material);

            log.warn("JWT_SECRET is not set. Generated a random signing key for this "
                    + "process — admin sessions will end whenever the service restarts. "
                    + "Set JWT_SECRET to a long random string to keep sessions stable.");
        } else {
            material = configuredSecret.getBytes(StandardCharsets.UTF_8);

            if (material.length < MIN_SECRET_BYTES) {
                throw new IllegalStateException(
                        "JWT_SECRET must be at least " + MIN_SECRET_BYTES
                                + " characters; got " + material.length);
            }
        }

        this.key = new SecretKeySpec(material, "HmacSHA256");
    }

    public String issueToken(String subject) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    /** Returns the subject for a valid token, or null when it is invalid or expired. */
    public String subjectOf(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            // Bad signature, malformed, or expired — all mean "not authenticated".
            return null;
        }
    }

    public long getExpirationMinutes() {
        return expirationMinutes;
    }

    /** Convenience for operators generating a secret to put in the environment. */
    public static String randomSecret() {
        byte[] bytes = new byte[48];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
