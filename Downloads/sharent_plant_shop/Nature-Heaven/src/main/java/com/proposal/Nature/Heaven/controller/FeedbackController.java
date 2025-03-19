package com.proposal.Nature.Heaven.controller;

import com.proposal.Nature.Heaven.model.Feedback;
import com.proposal.Nature.Heaven.model.User;
import com.proposal.Nature.Heaven.service.FeedbackService;
import com.proposal.Nature.Heaven.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String feedbackPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Add a new Feedback object for the form
        model.addAttribute("feedback", new Feedback());

        // Fetch all feedback to display on the page
        List<Feedback> allFeedback = feedbackService.getAllFeedback();
        model.addAttribute("allFeedback", allFeedback);

        // Add the logged-in user's details (if available)
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("currentUser", user);
        }

        return "feedback"; // Return the Thymeleaf template name (feedback.html)
    }

    @PostMapping
    public String submitFeedback(@ModelAttribute Feedback feedback, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            throw new RuntimeException("User must be logged in to submit feedback.");
        }

        // Log the username from UserDetails
        String username = userDetails.getUsername();
        System.out.println("Logged-in username: " + username);

        // Retrieve the logged-in user from the database
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found in the database for username: " + username);
        }

        // Associate the feedback with the logged-in user
        feedback.setUser(user);

        // Save the feedback
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        System.out.println("Feedback saved: " + savedFeedback);

        // Redirect to the feedback page to display the updated list
        return "redirect:/feedback/";
    }





    @GetMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(id);

        if (existingFeedback.isPresent()) {
            feedbackService.deleteFeedback(id);
            redirectAttributes.addFlashAttribute("message", "Feedback deleted successfully!");
            return "redirect:/feedback"; // Redirect to the feedback list page
        } else {
            redirectAttributes.addFlashAttribute("error", "Feedback not found!");
            return "redirect:/feedback"; // Redirect to the feedback list page with error message
        }
    }



















}