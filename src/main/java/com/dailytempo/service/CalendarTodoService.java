package com.dailytempo.service;

import com.dailytempo.domain.DailyTodo;
import com.dailytempo.domain.TodoItem;
import com.dailytempo.domain.User;
import com.dailytempo.dto.CalendarDay;
import com.dailytempo.dto.ExerciseIconOption;
import com.dailytempo.repository.DailyTodoRepository;
import com.dailytempo.repository.TodoItemRepository;
import com.dailytempo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CalendarTodoService {

    private static final List<ExerciseIconOption> ICON_OPTIONS = List.of(
            new ExerciseIconOption("run", "달리기", "#ff6b5f"),
            new ExerciseIconOption("upper", "상체 운동", "#6c63ff"),
            new ExerciseIconOption("lower", "하체 운동", "#2ca58d"),
            new ExerciseIconOption("dumbbell", "웨이트", "#f2a900"),
            new ExerciseIconOption("yoga", "요가", "#d65db1"),
            new ExerciseIconOption("cycle", "자전거", "#168aad"),
            new ExerciseIconOption("swim", "수영", "#277da1"),
            new ExerciseIconOption("stretch", "스트레칭", "#7a9e3a")
    );

    private final TodoItemRepository todoItemRepository;
    private final DailyTodoRepository dailyTodoRepository;
    private final UserRepository userRepository;

    public CalendarTodoService(
            TodoItemRepository todoItemRepository,
            DailyTodoRepository dailyTodoRepository,
            UserRepository userRepository
    ) {
        this.todoItemRepository = todoItemRepository;
        this.dailyTodoRepository = dailyTodoRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoItem> findTodoItems(String username) {
        return todoItemRepository.findAllByUserUsernameOrderByIdAsc(username);
    }

    public List<ExerciseIconOption> iconOptions() {
        return ICON_OPTIONS;
    }

    @Transactional(readOnly = true)
    public List<CalendarDay> buildMonth(String username, YearMonth month) {
        LocalDate firstDay = month.atDay(1);
        LocalDate gridStart = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate gridEnd = gridStart.plusDays(41);

        Map<LocalDate, Set<Long>> completedByDate = dailyTodoRepository
                .findAllByTodoItemUserUsernameAndTodoDateBetween(username, gridStart, gridEnd)
                .stream()
                .filter(DailyTodo::isCompleted)
                .collect(Collectors.groupingBy(
                        DailyTodo::getTodoDate,
                        Collectors.mapping(todo -> todo.getTodoItem().getId(), Collectors.toSet())
                ));

        List<CalendarDay> days = new ArrayList<>(42);
        LocalDate today = LocalDate.now();
        for (int index = 0; index < 42; index++) {
            LocalDate date = gridStart.plusDays(index);
            days.add(new CalendarDay(
                    date,
                    date.getDayOfMonth(),
                    YearMonth.from(date).equals(month),
                    date.equals(today),
                    completedByDate.getOrDefault(date, Set.of())
            ));
        }
        return days;
    }

    @Transactional
    public void addTodoItem(String username, String iconKey) {
        ExerciseIconOption option = ICON_OPTIONS.stream()
                .filter(candidate -> candidate.key().equals(iconKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 운동 아이콘입니다."));
        if (todoItemRepository.existsByUserUsernameAndIconKey(username, iconKey)) {
            return;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        TodoItem item = new TodoItem();
        item.setUser(user);
        item.setName(option.label());
        item.setIconKey(option.key());
        todoItemRepository.save(item);
    }

    @Transactional
    public void deleteTodoItem(String username, Long itemId) {
        TodoItem item = findOwnedItem(username, itemId);
        dailyTodoRepository.deleteAllByTodoItemId(item.getId());
        todoItemRepository.delete(item);
    }

    @Transactional
    public void updateCompletion(
            String username,
            Long itemId,
            LocalDate date,
            boolean completed
    ) {
        TodoItem item = findOwnedItem(username, itemId);
        DailyTodo dailyTodo = dailyTodoRepository
                .findByTodoItemIdAndTodoDate(itemId, date)
                .orElseGet(() -> {
                    DailyTodo created = new DailyTodo();
                    created.setTodoItem(item);
                    created.setTodoDate(date);
                    return created;
                });
        dailyTodo.setCompleted(completed);
        dailyTodoRepository.save(dailyTodo);
    }

    private TodoItem findOwnedItem(String username, Long itemId) {
        return todoItemRepository.findByIdAndUserUsername(itemId, username)
                .orElseThrow(() -> new IllegalArgumentException("TODO 항목을 찾을 수 없습니다."));
    }
}
