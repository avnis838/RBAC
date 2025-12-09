package com.example.RBACUserManagement.controller;


import com.example.RBACUserManagement.dto.UserStatsDTO;
import com.example.RBACUserManagement.model.User;
import com.example.RBACUserManagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        List<UserStatsDTO> users = userService.getAllUsersWithLastLogin();

        return ResponseEntity.ok(
                Map.of(
                        "totalUsers", users.size(),
                        "users", users
                )
        );
    }
}

