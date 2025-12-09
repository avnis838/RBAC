package com.example.RBACUserManagement.service;


import com.example.RBACUserManagement.dto.AuthResponse;
import com.example.RBACUserManagement.dto.LoginRequest;
import com.example.RBACUserManagement.model.User;
import com.example.RBACUserManagement.repository.UserRepository;
import com.example.RBACUserManagement.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final UserService userService;
    private final KafkaEventProducer eventProducer;

    public AuthService(AuthenticationManager authManager, JwtService jwtService,
                       UserRepository userRepo, UserService userService,
                       KafkaEventProducer eventProducer) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.userService = userService;
        this.eventProducer = eventProducer;
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Set<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        String token = jwtService.generateToken(user.getEmail(), roles);

        userService.updateLastLogin(user.getEmail());

        log.info("DEBUG: About to send login event for {}", user.getEmail());

        eventProducer.sendUserEvent("user-login", Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "timestamp", user.getLastLoginAt() == null ? java.time.Instant.now().toString() : user.getLastLoginAt().toString()
        ));

        return new AuthResponse(token);
    }
}

