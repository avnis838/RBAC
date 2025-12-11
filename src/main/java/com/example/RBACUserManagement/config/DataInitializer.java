package com.example.RBACUserManagement.config;

import com.example.RBACUserManagement.model.Role;
import com.example.RBACUserManagement.model.User;
import com.example.RBACUserManagement.repository.RoleRepository;
import com.example.RBACUserManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // Essential for securing the initial password

    @Override
    public void run(String... args) throws Exception {
        // 1. Initialize Roles
        Role adminRole = createOrGetRole("ADMIN");

        // 2. Initialize Users (and assign the role)
        createInitialAdminUser(adminRole);
    }

    /**
     * Helper method to create a role if it doesn't already exist.
     */
    private Role createOrGetRole(String roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);

        if (roleOptional.isEmpty()) {
            Role newRole = Role.builder()
                    .name(roleName)
                    .build();
            System.out.println("Creating role: " + roleName);
            return roleRepository.save(newRole);
        }

        System.out.println("Role already exists: " + roleName);
        return roleOptional.get();
    }

    /**
     * Helper method to create the initial admin user.
     */
    private void createInitialAdminUser(Role adminRole) {
        // Check if the initial user already exists to prevent duplicates on every startup
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {

            // Create and persist the initial Admin user
            User adminUser = User.builder()
                    .username("SystemAdmin")
                    .email("admin@example.com")
                    // ALWAYS encode passwords before saving them!
                    .password(passwordEncoder.encode("adminpassword"))
                    .createdAt(Instant.now())
                    .roles(Collections.singleton(adminRole)) // Assign the Admin role
                    .build();

            userRepository.save(adminUser);
            System.out.println("Created initial Admin user.");
        } else {
            System.out.println("Initial Admin user already exists.");
        }
    }
}