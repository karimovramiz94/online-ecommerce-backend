package org.example.service;
import org.example.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.example.CartItem;
import org.example.repo.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Генерирует конструктор для финальных полей (нужен Lombok)
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    // 1. READ: Получить все товары в корзине пользователя
    public List<CartItem> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public CartItem addItemToCart(CartItem item) {
        // 1. Достаем имя текущего пользователя из системы безопасности
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // 2. Находим этого пользователя в базе (чтобы получить его объект)
        // Убедись, что у тебя внедрен userRepository
        org.example.User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Logged in user not found in DB"));

        // 3. ПРИВЯЗЫВАЕМ пользователя к элементу корзины перед сохранением
        item.setUser(currentUser);

        // 4. Сохраняем элемент (теперь с привязанным userId)
        CartItem savedItem = cartRepository.save(item);

        // 5. Твоя логика: сбрасываем в базу и перечитываем для полной инфы
        cartRepository.flush();

        return cartRepository.findById(savedItem.getId())
                .orElseThrow(() -> new RuntimeException("Item not found after save"));
    }

    // 3. UPDATE: Изменить количество товара (например, 1 шт -> 3 шт)
    @Transactional
    public CartItem updateQuantity(Long itemId, int quantity) {
        CartItem item = cartRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Товар в корзине не найден с ID: " + itemId));

        item.setQuantity(quantity);
        return cartRepository.save(item);
    }

    // 4. DELETE: Удалить конкретную позицию из корзины
    @Transactional
    public void removeItemFromCart(Long itemId) {
        if (!cartRepository.existsById(itemId)) {
            throw new RuntimeException("Не удалось удалить: позиция не найдена");
        }
        cartRepository.deleteById(itemId);
    }

    // 5. DELETE ALL: Полностью очистить корзину пользователя
    @Transactional
    public void clearUserCart(Long userId) {
        List<CartItem> items = cartRepository.findByUserId(userId);
        if (items.isEmpty()) {
            // Если корзина уже пуста, ничего не делаем или кидаем ошибку
            return;
        }
        cartRepository.deleteAll(items);
    }
}