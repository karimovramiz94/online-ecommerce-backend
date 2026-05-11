package org.example.repo;

import org.example.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Можно добавить поиск по email
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}