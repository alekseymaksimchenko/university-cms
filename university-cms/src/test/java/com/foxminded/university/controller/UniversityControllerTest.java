package com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.foxminded.university.config.SecurityConfig;

@WebMvcTest(UniversityController.class)
@Import(SecurityConfig.class)
class UniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String WELCOME_MESSAGE = "WELCOME TO UNIVERSITY";

    @Test
    void testUniversityController_goHome_shouldPassWithPermitAll() throws Exception {

        mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", Matchers.is(WELCOME_MESSAGE)));
    }

}
