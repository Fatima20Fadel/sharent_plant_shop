package com.proposal.Nature.Heaven.security;

import com.proposal.Nature.Heaven.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login", "/about", "/api/plants/", "/css/**", "/js/**", "/img/**", "/lib/**", "/scss/**").permitAll()
                        .requestMatchers("/api/plants/add").hasRole("ADMIN")
                        .anyRequest().authenticated()  // Secure other endpoints
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler((request, response, authentication) -> {
                            authentication.getAuthorities().forEach(authority -> {
                                try {
                                    if (authority.getAuthority().equals("ROLE_ADMIN")) {
                                        response.sendRedirect("/api/plants/add"); // Redirect admin
                                    } else {
                                        response.sendRedirect("/api/plants/"); // Redirect normal users
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        })
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}