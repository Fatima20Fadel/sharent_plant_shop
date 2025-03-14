package com.proposal.Nature.Heaven.repository;

import com.proposal.Nature.Heaven.model.PlantGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantGuideRepository extends JpaRepository<PlantGuide, Long> {
}