package com.proposal.Nature.Heaven.controller;

import com.proposal.Nature.Heaven.model.Category;
import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.PlantGuide;
import com.proposal.Nature.Heaven.repository.CategoryRepository;
import com.proposal.Nature.Heaven.repository.PlantGuideRepository;
import com.proposal.Nature.Heaven.repository.PlantRepository;
import com.proposal.Nature.Heaven.service.CategoryService;
import com.proposal.Nature.Heaven.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    private final PlantGuideRepository plantGuideRepository;
    private final PlantRepository plantRepository;


    @Autowired
    public PlantController(PlantService plantService,CategoryService categoryService,CategoryRepository categoryRepository,
                           PlantGuideRepository plantGuideRepository,PlantRepository plantRepository) {
        this.plantService = plantService;
        this.categoryService=categoryService;
        this.plantGuideRepository=plantGuideRepository;
        this.plantRepository=plantRepository;
        this.categoryRepository=categoryRepository;
    }

    @GetMapping("/")
    public String getPlants(Model model) {
        List<Plant> plantsList = plantService.getAllPlants(); // Fetching all plants
        model.addAttribute("plantsList", plantsList); // Adding the list to the model
        return "plant";
    }

    // Get all plants
    @GetMapping
    public ResponseEntity<List<Plant>> getAllPlants() {
        List<Plant> plants = plantService.getAllPlants();
        return new ResponseEntity<>(plants, HttpStatus.OK);
    }

    @GetMapping("/plant/{id}/guide")
    public String viewPlantGuide(@PathVariable("id") Long plantId, Model model) {
        Optional<Plant> plant = plantService.getPlantById(plantId);

        if (plant.isPresent()) {
            model.addAttribute("plant", plant.get());
            return "plant-guide";
        } else {
            model.addAttribute("error", "Plant not found");
            return "error";
        }
    }



    // Get plant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {
        Optional<Plant> plant = plantService.getPlantById(id);
        return plant.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new plant
    @PostMapping
    public ResponseEntity<Plant> createPlant(@RequestBody Plant plant) {
        Plant savedPlant = plantService.savePlant(plant);
        return new ResponseEntity<>(savedPlant, HttpStatus.CREATED);
    }


    @GetMapping("/add")
    public String showAddPlantForm(Model model) {
        // Initialize the plant object and its associated plantGuide
        Plant plant = new Plant();
        plant.setPlantGuide(new PlantGuide());  // Ensure plantGuide is initialized

        model.addAttribute("plant", plant);

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "add-plant"; // Thymeleaf template name
    }


    // Handle form submission to add a new plant
    @PostMapping("/add")
    public String addPlant(@ModelAttribute("plant") Plant plant, Model model) {
        plantGuideRepository.save(plant.getPlantGuide());
        plantRepository.save(plant);
        model.addAttribute("plant", plant);
        return "redirect:/api/plants/";
    }


    // Update a plant via API (for RESTful calls)
    @PutMapping("/{id}")
    public ResponseEntity<Plant> updatePlant(@PathVariable Long id, @RequestBody Plant plant) {
        Optional<Plant> existingPlant = plantService.getPlantById(id);
        if (existingPlant.isPresent()) {
            plant.setId(id); // Ensure we are updating the existing plant
            Plant updatedPlant = plantService.savePlant(plant); // Save the updated plant
            return new ResponseEntity<>(updatedPlant, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }




    // Delete a plant
    @GetMapping ("/delete/{id}")
    public String deletePlant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Plant> existingPlant = plantService.getPlantById(id);

        if (existingPlant.isPresent()) {
            List<Plant> plants = existingPlant.get().getCategory().getPlants();
            plants.remove(existingPlant.get());
            Category category= categoryRepository.getById(existingPlant.get().getCategory().getId());
            category.setPlants(plants);
            categoryRepository.save(category);
            plantGuideRepository.deleteById(existingPlant.get().getPlantGuide().getId());

            plantService.deletePlant(id);
            redirectAttributes.addFlashAttribute("message", "Plant deleted successfully!");
            return "redirect:/api/plants/"; // Redirect to the plant list page
        } else {
            redirectAttributes.addFlashAttribute("error", "Plant not found!");
            return "redirect:/api/plants/"; // Redirect to the plant list page with error message
        }
    }


    // Updating a plant along with its guide
    @PostMapping("/update/{id}")
    public String updatePlant(@PathVariable Long id, @ModelAttribute("plant") Plant plant, RedirectAttributes redirectAttributes) {
        Optional<Plant> existingPlant = plantService.getPlantById(id);
        if (existingPlant.isPresent()) {
            plant.setId(id); // Ensure we are updating the existing plant
            plantGuideRepository.save(plant.getPlantGuide());
            plantRepository.save(plant);
            redirectAttributes.addFlashAttribute("message", "Plant and guide updated successfully!");
            return "redirect:/api/plants/"; // Redirect after successful update
        } else {
            redirectAttributes.addFlashAttribute("error", "Plant not found!");
            return "redirect:/api/plants/"; // Redirect to the list page with error
        }
    }



    @GetMapping("/update/{id}")
    public String showUpdated (@PathVariable long id,Model model) {
        // Initialize the plant object and its associated plantGuide
        Plant plant = plantService.getPlantById(id).get();
        plant.setPlantGuide(plant.getPlantGuide());

        model.addAttribute("plant", plant);

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "update-plant"; // Thymeleaf template name
    }



}
