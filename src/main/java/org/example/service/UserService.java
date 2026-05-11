package org.example.service;

import org.example.User; // Убедись, что путь верный
import org.example.exception.ResourceNotFoundException;
import org.example.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Добавляем шифровальщик

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // НОВЫЙ МЕТОД: Регистрация пользователя
    public User registerUser(User user) {
        // 1. Шифруем пароль (из "123" делаем "$2a$10$...")
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 2. Назначаем роль по умолчанию (обязательно с префиксом ROLE_)
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        // 3. Сохраняем
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User updateUser(Long id, User details) {
        User user = getUserById(id);

        user.setUsername(details.getUsername());
        user.setEmail(details.getEmail());

        // Если при обновлении пришел новый пароль, его тоже нужно захешировать
        if (details.getPassword() != null && !details.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(details.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
