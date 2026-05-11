package org.example.service;

import org.example.CartItem;
import org.example.Order;
import org.example.OrderItem;
import org.example.User;
import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public OrderService(OrderRepository orderRepository,
                        CartService cartService,
                        UserService userService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
    }

    @Transactional
    public Order placeOrder(Long userId) {
        // 1. Получаем пользователя.
        // Если в твоем UserService.getUserById уже встроено исключение,
        // то этот метод сам прервет выполнение, если юзера нет.
        User user = userService.getUserById(userId);

        // 2. Получаем список товаров из корзины этого пользователя
        List<CartItem> cartItems = cartService.getUserCart(userId);

        // 3. Проверяем, не пуста ли корзина (наше кастомное исключение BadRequest)
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cannot place order: Your cart is empty!");
        }

        // 4. Создаем новый объект Заказа
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 5. Конвертируем CartItem (временное) в OrderItem (постоянное)
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            // Фиксируем цену на момент покупки, чтобы история не менялась при изменении цен в магазине
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            // Считаем общую сумму заказа
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        // 6. Устанавливаем заполненные данные в заказ
        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        // 7. Сохраняем заказ в базу данных (сохранится и сам заказ, и элементы order_items)
        Order savedOrder = orderRepository.save(order);

        // 8. Очищаем корзину пользователя, так как заказ успешно оформлен
        cartService.clearUserCart(userId);

        return savedOrder;
    }
}
