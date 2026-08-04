package com.dailytempo.controller;

import com.dailytempo.domain.TempoBox;
import com.dailytempo.dto.TempoBoxRequest;
import com.dailytempo.service.TempoBoxService;
import com.dailytempo.domain.TempoType;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TempoBoxController {

    private final TempoBoxService tempoBoxService;

    public TempoBoxController(TempoBoxService tempoBoxService) {
        this.tempoBoxService = tempoBoxService;
    }

    @PostMapping("/custom")
    public String create(
            @Valid @ModelAttribute("form") TempoBoxRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "커스텀");
            return "custom";
        }

        TempoBox saved = tempoBoxService.create(authentication.getName(), request);
        String path = saved.getType() == TempoType.DOT ? "/exercise_dot" : "/exercise";
        return "redirect:" + path + "?id=" + saved.getId();
    }
}
