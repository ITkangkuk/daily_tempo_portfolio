package com.dailytempo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(
        name = "daily_todos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_daily_todos_item_date",
                columnNames = {"todo_item_id", "todo_date"}
        )
)
public class DailyTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todo_item_id", nullable = false)
    private TodoItem todoItem;

    @Column(name = "todo_date", nullable = false)
    private LocalDate todoDate;

    @Column(nullable = false)
    private boolean completed;

    public TodoItem getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(TodoItem todoItem) {
        this.todoItem = todoItem;
    }

    public LocalDate getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(LocalDate todoDate) {
        this.todoDate = todoDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
