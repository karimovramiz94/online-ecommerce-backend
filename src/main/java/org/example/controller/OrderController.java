package org.example.controller;

import org.example.Order;
import org.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Получить все заказы пользователя
    // URL: GET /api/ecommerce/orders/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // 2. Создать заказ (Оформить корзину)
    // URL: POST /api/ecommerce/orders/place/1
    @PostMapping("/place/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}