package org.example.controller;

import org.example.CartItem;
import org.example.service.CartService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/users/{userId}/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // GET /api/ecommerce/users/{userId}/cart
    @GetMapping
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    // POST /api/ecommerce/users/{userId}/cart/products/{productId}
    @PostMapping("/products/{productId}")
    public CartItem addToCart(@PathVariable Long userId, @PathVariable Long productId) {
        return cartService.addToCart(userId, productId);
    }
}