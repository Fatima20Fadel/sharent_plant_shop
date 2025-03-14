package com.proposal.Nature.Heaven.service;

import com.proposal.Nature.Heaven.model.User;
import com.proposal.Nature.Heaven.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        logger.info("Attempting to authenticate user: {}", username);

        if (userRepository == null) {
            throw new IllegalStateException("UserRepository is not initialized.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("User '{}' not found in the database.", username);
            throw new UsernameNotFoundException("Invalid credentials.");
        }

        // Log user details
        logger.info("User found: {} | Role: {}", user.getUsername(), user.getRole().name());

        // Ensure role has "ROLE_" prefix
        String roleWithPrefix = user.getRole().name().startsWith("ROLE_")
                ? user.getRole().name()
                : "ROLE_" + user.getRole().name();

        logger.info("Assigned authorities for user '{}': {}", user.getUsername(), roleWithPrefix);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Password should be encoded
                .authorities(roleWithPrefix) // Assign authorities
                .build();
    }

}







