package org.example.controller;

import org.example.Product;
import org.example.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }


    // GET /api/ecommerce/products/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    // Дополнительно: Получить один товар по ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        // Мы добавим этот метод в сервис чуть позже, если понадобится
        return productService.getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Обновить продукт: PUT /api/ecommerce/products/1
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails);
    }

    // Удалить продукт: DELETE /api/ecommerce/products/1
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}