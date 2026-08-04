package com.dailytempo.repository;

import com.dailytempo.domain.DailyTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyTodoRepository extends JpaRepository<DailyTodo, Long> {
    List<DailyTodo> findAllByTodoItemUserUsernameAndTodoDateBetween(
            String username,
            LocalDate start,
            LocalDate end
    );

    Optional<DailyTodo> findByTodoItemIdAndTodoDate(Long todoItemId, LocalDate todoDate);

    void deleteAllByTodoItemId(Long todoItemId);
}
