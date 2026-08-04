package com.dailytempo.controller;

import com.dailytempo.domain.TempoBox;
import com.dailytempo.dto.TempoBoxRequest;
import com.dailytempo.service.TempoBoxService;
import com.dailytempo.service.UserService;
import com.dailytempo.domain.TempoType;
import com.dailytempo.dto.RegistrationRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final TempoBoxService tempoBoxService;
    private final UserService userService;

    public PageController(TempoBoxService tempoBoxService, UserService userService) {
        this.tempoBoxService = tempoBoxService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "홈");
        return "index";
    }

    @GetMapping("/home")
    public String mainHome(Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "홈");
        model.addAttribute("currentUser", userService.findByUsername(authentication.getName()));
        model.addAttribute("tempoBoxes", tempoBoxService.findAll(authentication.getName()));
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "로그인");
        return "login";
    }

    @GetMapping("/membership")
    public String membership(Model model) {
        model.addAttribute("pageTitle", "회원가입");
        model.addAttribute("form", new RegistrationRequest("", "", "", "", "", ""));
        return "membership";
    }

    @GetMapping("/id")
    public String id(Model model) {
        model.addAttribute("pageTitle", "아이디 찾기");
        return "id";
    }

    @GetMapping("/pw")
    public String pw(Model model) {
        model.addAttribute("pageTitle", "비밀번호 찾기");
        return "pw";
    }

    @GetMapping("/exercise")
    public String exercise(
            @RequestParam Long id,
            Model model,
            Authentication authentication
    ) {
        TempoBox tempoBox = tempoBoxService.findOwned(id, authentication.getName());
        if (tempoBox.getType() == TempoType.DOT) {
            return "redirect:/exercise_dot?id=" + id;
        }
        model.addAttribute("pageTitle", "운동");
        model.addAttribute("tempo", tempoBox);
        return "exercise";
    }

    @GetMapping("/custom")
    public String custom(Model model) {
        model.addAttribute("pageTitle", "커스텀");
        model.addAttribute("form", new TempoBoxRequest("", TempoType.UPDOWN, "#ec6961"));
        return "custom";
    }

    @GetMapping("/exercise_dot")
    public String exerciseDot(
            @RequestParam Long id,
            Model model,
            Authentication authentication
    ) {
        TempoBox tempoBox = tempoBoxService.findOwned(id, authentication.getName());
        if (tempoBox.getType() == TempoType.UPDOWN) {
            return "redirect:/exercise?id=" + id;
        }
        model.addAttribute("pageTitle", "Dot 운동");
        model.addAttribute("tempo", tempoBox);
        return "exercise_dot";
    }

}
