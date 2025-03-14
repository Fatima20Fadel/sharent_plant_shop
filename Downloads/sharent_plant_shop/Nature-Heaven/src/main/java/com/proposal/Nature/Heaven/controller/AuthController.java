package com.proposal.Nature.Heaven.controller;

import org.springframework.ui.Model;
import com.proposal.Nature.Heaven.DTO.UserRegistrationDto;
import com.proposal.Nature.Heaven.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";  // View: templates/register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto,
                               @RequestParam String role) {
        userService.registerUser(registrationDto, role);  // Pass role here
        return "redirect:/auth/login";  // Redirect to login after registration
    }

    // Login form handling
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // This will render the login HTML page (login.html)
    }
}
