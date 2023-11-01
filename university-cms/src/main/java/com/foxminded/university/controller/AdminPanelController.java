package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.foxminded.university.service.ApplicationUserService;
import com.foxminded.university.web.dto.UserRegistrationDto;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminPanelController {

    private final ApplicationUserService applicationUserService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);

    public AdminPanelController(ApplicationUserService applicationUserService) {
           this.applicationUserService = applicationUserService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        model.addAttribute("users", applicationUserService.getAllApplicationUsers());
        return "admin";
    }

    @PostMapping("/admin")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {
        LOGGER.debug("Controller, UserDto from HTML form is ({})", userRegistrationDto);
        applicationUserService.saveUser(userRegistrationDto);
        return "redirect:/admin";

    }

}
