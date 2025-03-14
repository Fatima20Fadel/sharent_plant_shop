package com.proposal.Nature.Heaven.service;
import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.PlantGuide;
import com.proposal.Nature.Heaven.repository.PlantGuideRepository;
import com.proposal.Nature.Heaven.repository.PlantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;
    private PlantGuideRepository plantGuideRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    // Save Plant and PlantGuide
    public Plant savePlantWithGuide(Plant plant, PlantGuide guide) {
        PlantGuide savedGuide = plantGuideRepository.save(guide);
        plant.setPlantGuide(savedGuide);
        return plantRepository.save(plant);
    }

    // Get all plants
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    // Get a plant by its ID
    public Optional<Plant> getPlantById(Long id) {
        return plantRepository.findById(id);
    }

    // Save a plant
    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }


    public void deletePlant(Long id) {
       plantRepository.deleteById(id);}


    public List<Plant> getPlantsByCategory(Long categoryId) {
        return plantRepository.findByCategoryId(categoryId);
        }

    @Transactional
    public void updatePlantAndGuide(Plant plant) {
        plantGuideRepository.save(plant.getPlantGuide());
        plantRepository.save(plant);
    }

}
