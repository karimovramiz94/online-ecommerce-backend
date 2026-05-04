package org.example.repo;

import org.example.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Можно добавить поиск по email
    User findByEmail(String email);
}