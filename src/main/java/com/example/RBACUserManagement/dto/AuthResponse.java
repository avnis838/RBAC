package com.example.RBACUserManagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";

    public AuthResponse(String token){
        this.token = token;
    }
}

