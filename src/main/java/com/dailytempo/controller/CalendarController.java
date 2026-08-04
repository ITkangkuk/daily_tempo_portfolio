package com.dailytempo.controller;

import com.dailytempo.service.CalendarTodoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Controller
public class CalendarController {

    private final CalendarTodoService calendarTodoService;

    public CalendarController(CalendarTodoService calendarTodoService) {
        this.calendarTodoService = calendarTodoService;
    }

    @GetMapping({"/calendar", "/calender"})
    public String calendar(
            @RequestParam(required = false) String month,
            Authentication authentication,
            Model model
    ) {
        YearMonth selectedMonth = parseMonth(month);
        String username = authentication.getName();

        model.addAttribute("pageTitle", "캘린더");
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("previousMonth", selectedMonth.minusMonths(1));
        model.addAttribute("nextMonth", selectedMonth.plusMonths(1));
        model.addAttribute("calendarDays", calendarTodoService.buildMonth(username, selectedMonth));
        model.addAttribute("todoItems", calendarTodoService.findTodoItems(username));
        model.addAttribute("iconOptions", calendarTodoService.iconOptions());
        return "calender";
    }

    @PostMapping("/calendar/todos")
    public String addTodo(
            @RequestParam String iconKey,
            @RequestParam String month,
            Authentication authentication
    ) {
        calendarTodoService.addTodoItem(authentication.getName(), iconKey);
        return redirectToMonth(month);
    }

    @PostMapping("/calendar/todos/delete")
    public String deleteTodo(
            @RequestParam Long itemId,
            @RequestParam String month,
            Authentication authentication
    ) {
        calendarTodoService.deleteTodoItem(authentication.getName(), itemId);
        return redirectToMonth(month);
    }

    @PostMapping("/calendar/completion")
    @ResponseBody
    public Map<String, Boolean> updateCompletion(
            @RequestParam Long itemId,
            @RequestParam LocalDate date,
            @RequestParam boolean completed,
            Authentication authentication
    ) {
        calendarTodoService.updateCompletion(
                authentication.getName(),
                itemId,
                date,
                completed
        );
        return Map.of("completed", completed);
    }

    private YearMonth parseMonth(String month) {
        if (month == null || month.isBlank()) {
            return YearMonth.now();
        }
        try {
            return YearMonth.parse(month);
        } catch (DateTimeParseException exception) {
            return YearMonth.now();
        }
    }

    private String redirectToMonth(String month) {
        return "redirect:/calendar?month=" + parseMonth(month);
    }
}
