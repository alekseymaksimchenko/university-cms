package com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversityController {

    private static final String WELCOME_MESSAGE = "WELCOME TO UNIVERSITY";

    @GetMapping("/")
    public String goHome(Model model) {
        model.addAttribute("message", WELCOME_MESSAGE);
        return "index";
    }
}
