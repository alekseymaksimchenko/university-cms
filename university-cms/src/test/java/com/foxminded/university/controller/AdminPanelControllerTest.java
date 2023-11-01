package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.foxminded.university.model.ApplicationUser;
import com.foxminded.university.service.ApplicationUserService;
import com.foxminded.university.web.dto.UserRegistrationDto;

@WebMvcTest(AdminPanelController.class)
class AdminPanelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationUserService applicationUserService;

    private static final List<ApplicationUser> TEST_USERS = new ArrayList<>(Arrays.asList(new ApplicationUser()));

    @Test
    @WithMockUser
    void testAdminPanelControlle_showAllUsers_shouldPass() throws Exception {
        when(applicationUserService.getAllApplicationUsers()).thenReturn(TEST_USERS);

        mockMvc.perform(get("/admin")).andExpect(status().isOk()).andExpect(view().name("admin"))
                .andExpect(model().attributeExists("users")).andExpect(model().attribute("users", Matchers.hasSize(1)))
                .andExpect(model().attribute("users", Matchers.contains(TEST_USERS.get(0))));
    }

    @Test
    @WithAnonymousUser
    void testAdminPanelControlle_showAllUsers_shouldNotPassInCaseUnauthorizedUser() throws Exception {
        when(applicationUserService.getAllApplicationUsers()).thenReturn(TEST_USERS);

        mockMvc.perform(get("/admin")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test", roles = "ADMIN")
    void testAdminPanelControlle_registerUser_shouldPass() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        ApplicationUser applicationUser = new ApplicationUser();
        when(applicationUserService.saveUser(userRegistrationDto)).thenReturn(applicationUser);

        mockMvc.perform(get("/admin").flashAttr("user", userRegistrationDto))
                .andExpect(status().isOk()).andExpect(view().name("admin"));

    }

}
