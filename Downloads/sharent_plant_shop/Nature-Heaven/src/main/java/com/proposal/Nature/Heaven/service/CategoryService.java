package com.proposal.Nature.Heaven.service;


import com.proposal.Nature.Heaven.model.Category;
import com.proposal.Nature.Heaven.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private static CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }


    public Optional<Category> getCategory(Long id){
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category,  MultipartFile file) throws IOException {
        category.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        category.setPlants(new ArrayList<>());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


}
