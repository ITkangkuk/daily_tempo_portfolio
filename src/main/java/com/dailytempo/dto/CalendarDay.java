package com.dailytempo.dto;

import java.time.LocalDate;
import java.util.Set;

public record CalendarDay(
        LocalDate date,
        int dayOfMonth,
        boolean currentMonth,
        boolean today,
        Set<Long> completedTodoIds
) {
}
