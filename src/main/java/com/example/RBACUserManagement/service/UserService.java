package com.example.RBACUserManagement.service;


import com.example.RBACUserManagement.dto.UserProfileDto;
import com.example.RBACUserManagement.dto.RegisterRequest;
import com.example.RBACUserManagement.dto.UserStatsDTO;
import com.example.RBACUserManagement.mapper.UserMapper;
import com.example.RBACUserManagement.model.Role;
import com.example.RBACUserManagement.model.User;
import com.example.RBACUserManagement.repository.RoleRepository;
import com.example.RBACUserManagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final KafkaEventProducer eventProducer;

    public UserService(UserRepository userRepo, RoleRepository roleRepo,
                       PasswordEncoder passwordEncoder, UserMapper userMapper,
                       KafkaEventProducer eventProducer) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public User register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .createdAt(Instant.now())
                .build();

        User saved = userRepo.save(user);

        // Send event
        eventProducer.sendUserEvent("user-registered", Map.of(
                "userId", saved.getId(),
                "email", saved.getEmail(),
                "timestamp", saved.getCreatedAt().toString()
        ));

        return saved;
    }

    public UserProfileDto getProfile(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toProfileDto(user);
    }

    @Transactional
    public void assignRoles(Long userId, Set<String> roleNames) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Set<Role> roles = new HashSet<>();
        for (String rname : roleNames) {
            Role role = roleRepo.findByName(rname).orElseGet(() -> roleRepo.save(new Role(null, rname)));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepo.save(user);
    }

    public List<User> countUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public void updateLastLogin(String email) {
        userRepo.findByEmail(email).ifPresent(u -> {
            u.setLastLoginAt(Instant.now());
            userRepo.save(u);
        });
    }

    public List<UserStatsDTO> getAllUsersWithLastLogin() {
        List<User> users = userRepo.findAll();

        List<UserStatsDTO> result = new ArrayList<>();

        for (User u : users) {
            UserStatsDTO dto = new UserStatsDTO(
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getLastLoginAt()
            );
            result.add(dto);
        }

        return result;
    }

}

