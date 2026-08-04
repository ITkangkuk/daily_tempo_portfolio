package com.dailytempo.repository;

import com.dailytempo.domain.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findAllByUserUsernameOrderByIdAsc(String username);
    Optional<TodoItem> findByIdAndUserUsername(Long id, String username);
    boolean existsByUserUsernameAndIconKey(String username, String iconKey);
}
