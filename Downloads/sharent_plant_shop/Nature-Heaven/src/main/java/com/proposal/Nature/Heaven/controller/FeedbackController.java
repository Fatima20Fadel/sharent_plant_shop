package com.proposal.Nature.Heaven.controller;
import com.proposal.Nature.Heaven.model.Feedback;
import com.proposal.Nature.Heaven.model.User;
import com.proposal.Nature.Heaven.service.FeedbackService;
import com.proposal.Nature.Heaven.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    // Save feedback (only for logged-in users)
    @PostMapping
    public Feedback submitFeedback(@RequestBody Feedback feedback, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("User must be logged in to submit feedback.");
        }

        User user = userService.findByEmail(userDetails.getUsername()); // Get logged-in user
        feedback.setUser(user);
        return feedbackService.saveFeedback(feedback);
    }

    // Get all feedback
    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }
}
