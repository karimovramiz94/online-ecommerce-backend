package org.example.controller;

import org.example.CartItem;
import org.example.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/cart") // Общий путь для всех методов в этом файле
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Получить корзину: GET /api/ecommerce/cart/1
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    // Добавить: POST /api/ecommerce/cart/add
    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(item));
    }

    // Обновить количество: PATCH /api/ecommerce/cart/item/5?quantity=3
    @PatchMapping("/item/{itemId}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long itemId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(itemId, quantity));
    }

    // Удалить один товар: DELETE /api/ecommerce/cart/item/5
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.noContent().build();
    }

    // Очистить всю корзину: DELETE /api/ecommerce/cart/clear/1
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearUserCart(userId);
        return ResponseEntity.noContent().build();
    }
}