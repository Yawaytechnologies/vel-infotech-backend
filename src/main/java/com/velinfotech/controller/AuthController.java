package com.velinfotech.controller;

import com.velinfotech.dto.LoginRequest;
import com.velinfotech.dto.LoginResponse;
import com.velinfotech.security.JwtService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${admin.username:}")
    private String adminUsername;

    /** Preferred: a bcrypt hash, so the plaintext never sits in configuration. */
    @Value("${admin.password-hash:}")
    private String adminPasswordHash;

    /** Fallback for simpler deployments; hashed in memory at startup. */
    @Value("${admin.password:}")
    private String adminPassword;

    private String effectiveHash;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostConstruct
    void init() {
        if (adminPasswordHash != null && !adminPasswordHash.isBlank()) {
            effectiveHash = adminPasswordHash;
        } else if (adminPassword != null && !adminPassword.isBlank()) {
            effectiveHash = passwordEncoder.encode(adminPassword);
        } else {
            effectiveHash = null;
        }

        if (effectiveHash == null || adminUsername == null || adminUsername.isBlank()) {
            // Fail closed. Without credentials nobody can sign in, which is far better
            // than falling back to a default everyone knows.
            log.error("Admin credentials are not configured. Set ADMIN_USERNAME and "
                    + "ADMIN_PASSWORD (or ADMIN_PASSWORD_HASH). Admin login is disabled "
                    + "until they are, and protected endpoints will reject every request.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        if (effectiveHash == null || adminUsername == null || adminUsername.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("message", "Admin login is not configured on the server."));
        }

        // Compare both factors before answering, and use a constant-time comparison for
        // the username so responses do not leak whether the username alone was right.
        boolean userOk = MessageDigest.isEqual(
                adminUsername.getBytes(StandardCharsets.UTF_8),
                request.getUsername().getBytes(StandardCharsets.UTF_8));

        boolean passOk = passwordEncoder.matches(request.getPassword(), effectiveHash);

        if (!userOk || !passOk) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }

        String token = jwtService.issueToken(adminUsername);

        return ResponseEntity.ok(new LoginResponse(
                token, adminUsername, jwtService.getExpirationMinutes() * 60));
    }

    /** Lets the console check a stored token is still good before showing the UI. */
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String subject = jwtService.subjectOf(authorization.substring(7).trim());

        if (subject == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(Map.of("username", subject));
    }
}
