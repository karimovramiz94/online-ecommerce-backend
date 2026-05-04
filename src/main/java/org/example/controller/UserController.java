package org.example.controller;

import org.example.User;
import org.example.repo.UserRepository;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository; // Репозиторий оставим только для простых операций типа findAll

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Создать нового пользователя
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Получить данные пользователя по ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Список всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
