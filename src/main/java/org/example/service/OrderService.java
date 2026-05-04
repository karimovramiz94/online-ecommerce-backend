package org.example.service;

import org.example.CartItem;
import org.example.Order;
import org.example.User;
import org.example.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository,
                        CartService cartService,
                        UserService userService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional // ОЧЕНЬ ВАЖНО: если что-то упадет, корзина не удалится
    public Order placeOrder(Long userId) {
        User user = userService.getUserById(userId);
        List<CartItem> cartItems = cartService.getCartByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(cartItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cartItems);

        return savedOrder;
    }
}
