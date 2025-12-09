package com.example.RBACUserManagement.service;


import com.example.RBACUserManagement.model.Role;
import com.example.RBACUserManagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role createRole(String name) {
        if (roleRepo.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Role already exists");
        }
        return roleRepo.save(new Role(null, name));
    }
}

