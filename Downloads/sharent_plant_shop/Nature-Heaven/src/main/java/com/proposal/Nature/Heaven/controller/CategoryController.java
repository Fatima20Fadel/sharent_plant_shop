package com.proposal.Nature.Heaven.controller;

import com.proposal.Nature.Heaven.model.Category;
import com.proposal.Nature.Heaven.model.Plant;
import com.proposal.Nature.Heaven.model.PlantGuide;
import com.proposal.Nature.Heaven.service.CategoryService;
import com.proposal.Nature.Heaven.service.PlantService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final PlantService plantService;

    @Autowired
    public CategoryController(CategoryService categoryService, PlantService plantService) {
        this.categoryService = categoryService;
        this.plantService = plantService;
    }

    @GetMapping(value = "/{categoryId}/image")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable Long categoryId) {
        Optional<Category> categoryOptional = categoryService.getCategory(categoryId);
        if (categoryOptional.isPresent()) {
            Category product = categoryOptional.get();
            byte[] imageBytes = java.util.Base64.getDecoder().decode(product.getImage());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new byte[0], HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        return "category";
    }

    @GetMapping("/add")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "add-category";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category, @RequestParam("imageFile") MultipartFile imageFile) throws IOException, IOException {
        categoryService.saveCategory(category, imageFile);
        return "redirect:/api/categories/";
    }

    @GetMapping("/{categoryId}/plants")
    public String viewPlantsByCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        List<Plant> plantsList = plantService.getPlantsByCategory(categoryId);
        if (plantsList == null || plantsList.isEmpty()) {
            System.out.println("No plants found for category " + categoryId);
        } else {
            System.out.println("Plants found: " + plantsList.size());
        }
        model.addAttribute("plants", plantsList);
        return "plantsByCategory";
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Optional<Category> existingCategory = categoryService.getCategory(id);
        if (existingCategory.isPresent()) {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Category> existingCategory = categoryService.getCategory(id);

        if (existingCategory.isPresent()) {
            // Remove the category
            categoryService.deleteCategory(id); // Call the service to delete the category

            redirectAttributes.addFlashAttribute("message", "Category deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category not found!");
        }

        return "redirect:/api/categories/"; // Redirect to the category list page
    }


}
