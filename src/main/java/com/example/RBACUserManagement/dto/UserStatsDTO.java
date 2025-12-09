package com.example.RBACUserManagement.dto;

import java.time.Instant;

public record UserStatsDTO(
        Long id,
        String username,
        String email,
        Instant lastLoginAt
) {}