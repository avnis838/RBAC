package com.example.RBACUserManagement.dto;


import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}

