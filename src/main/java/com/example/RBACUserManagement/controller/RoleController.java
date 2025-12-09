package com.example.RBACUserManagement.controller;


import com.example.RBACUserManagement.dto.RoleDto;
import com.example.RBACUserManagement.service.RoleService;
import com.example.RBACUserManagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles")
    public ResponseEntity<RoleDto> createRole(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        var role = roleService.createRole(name);
        var dto = new RoleDto(role.getId(), role.getName());
        return ResponseEntity.status(201).body(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<Void> assignRoles(@PathVariable Long userId, @RequestBody Set<String> roles) {
        userService.assignRoles(userId, roles);
        return ResponseEntity.ok().build();
    }
}

