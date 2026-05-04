package org.example.service;

import org.example.CartItem;
import org.example.Product;
import org.example.User;
import org.example.repo.CartRepository;
import org.example.repo.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserService userService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public List<CartItem> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(Long userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(1);

        return cartRepository.save(item);
    }

    public void clearCart(List<CartItem> items) {
        cartRepository.deleteAll(items);
    }
}
