package org.example.service;

import org.example.Category;
import org.example.exception.ResourceNotFoundException;
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

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    // Метод для создания новой категории
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category details) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(details.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
