package org.example.service;

import org.example.Category;
import org.example.repo.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Метод для получения всех категорий
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Метод для создания новой категории
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
