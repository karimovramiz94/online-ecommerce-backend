package org.example.controller;

import org.example.Category;
import org.example.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Говорит, что этот класс обрабатывает HTTP-запросы (JSON)
@RequestMapping("/api/ecommerce/categories") // Базовый адрес для всех запросов в этом классе
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // На этот адрес придет запрос GET http://localhost:8080/api/categories
    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    // На этот адрес придет запрос POST http://localhost:8080/api/categories
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category details) {
        return categoryService.updateCategory(id, details);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
