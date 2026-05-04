package org.example.controller;

import org.example.Order;
import org.example.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET /api/ecommerce/users/{userId}/orders
    @GetMapping
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    // POST /api/ecommerce/users/{userId}/orders
    @PostMapping
    public Order placeOrder(@PathVariable Long userId) {
        return orderService.placeOrder(userId);
    }
}