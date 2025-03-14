package com.proposal.Nature.Heaven.service;

import com.proposal.Nature.Heaven.model.PlantGuide;
import com.proposal.Nature.Heaven.repository.PlantGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantGuideService {

    @Autowired
    private PlantGuideRepository plantGuideRepository;

    public void save(PlantGuide guide) {
        plantGuideRepository.save(guide);
    }
}
