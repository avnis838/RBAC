package com.example.RBACUserManagement.controller;


import com.example.RBACUserManagement.dto.*;
import com.example.RBACUserManagement.model.User;
import com.example.RBACUserManagement.service.AuthService;
import com.example.RBACUserManagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserProfileDto> register(@Valid @RequestBody RegisterRequest req) {
        User saved = userService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.getProfile(saved.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> me(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getProfile(email));
    }
}

