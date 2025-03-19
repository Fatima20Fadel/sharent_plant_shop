package com.proposal.Nature.Heaven.service;

import com.proposal.Nature.Heaven.model.Feedback;
import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
}
