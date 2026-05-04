package org.example.repo;

import org.example.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Говорит Spring, что это компонент для работы с данными
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
