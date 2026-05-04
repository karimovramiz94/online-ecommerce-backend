package org.example.controller;

import org.example.Product;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/products")
public class ProductController {

    private final ProductService productService;

    // Spring сам найдет ProductService и подставит его сюда (Dependency Injection)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Получить список всех товаров
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Добавить новый товар
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // GET /api/ecommerce/products/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

//    // Дополнительно: Получить один товар по ID
//    @GetMapping("/{id}")
//    public Product getProductById(@PathVariable Long id) {
//        // Мы добавим этот метод в сервис чуть позже, если понадобится
//        return productService.getAllProducts().stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst()
//                .orElse(null);
//    }
}