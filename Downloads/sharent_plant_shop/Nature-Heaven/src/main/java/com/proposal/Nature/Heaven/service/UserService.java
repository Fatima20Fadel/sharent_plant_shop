package com.proposal.Nature.Heaven.service;

import com.proposal.Nature.Heaven.DTO.UserRegistrationDto;
import com.proposal.Nature.Heaven.model.User;
import com.proposal.Nature.Heaven.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User registerUser(UserRegistrationDto registrationDto, String role) {
        logger.info("Registering user: {}", registrationDto.getUsername());

        // Check if the username or email already exists
        if (userRepository.findByUsername(registrationDto.getUsername()) != null) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.findByEmail(registrationDto.getEmail()) != null) {
            throw new RuntimeException("Email already taken");
        }
        // Ensure only one admin exists
        if ("ROLE_ADMIN".equals(role)) {
            if (isAdminExist()) {
                throw new RuntimeException("Only one admin is allowed.");
            }
        }
        // Create and save user
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }
        try {
            user.setRole(User.Role.valueOf(role)); // Directly maps to enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role specified");
        }

        // Save user
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getUsername());
        return user;
    }
    private boolean isAdminExist() {
        return userRepository.findAll().stream().anyMatch(user -> user.getRole() == User.Role.ROLE_ADMIN);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<User> findUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username); // Use a method in repository
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public Set<String> getDefaultRoles() {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        return roles;
    }
    // Method to get all users with ROLE_USER
    public List<User> getAllUsersWithRoleUser() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user.getRole() == User.Role.ROLE_USER)
                .collect(Collectors.toList());
    }

}

