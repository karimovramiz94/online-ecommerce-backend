package org.example.service;

import org.example.Product;
import org.example.repo.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
                return productRepository.save(product);
    }



    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }



    // Обновление товара
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        // Можно обновить и другие поля, например категорию
        return productRepository.save(product);
    }

    // Удаление товара
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

}
