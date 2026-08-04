package com.dailytempo.controller;

import com.dailytempo.dto.RegistrationRequest;
import com.dailytempo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/membership")
    public String register(
            @Valid @ModelAttribute("form") RegistrationRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("pageTitle", "회원가입");

        if (!normalized(request.password()).equals(normalized(request.passwordConfirm()))) {
            bindingResult.rejectValue(
                    "passwordConfirm",
                    "password.mismatch",
                    "비밀번호가 일치하지 않습니다."
            );
        }

        if (bindingResult.hasErrors()) {
            return "membership";
        }

        try {
            userService.register(request);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("registrationError", exception.getMessage());
            return "membership";
        }
    }

    private String normalized(String value) {
        return value == null ? "" : value.trim();
    }
}
