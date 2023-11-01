package com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.foxminded.university.config.SecurityConfig;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.web.dto.GroupDto;

@WebMvcTest(GroupController.class)
@Import({ SecurityConfig.class, TestUserDetailsService.class })
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupController groupController;

    @MockBean
    private GroupService groupService;

    private static final List<Group> TEST_GROUPS = new ArrayList<>(Arrays.asList(new Group()));
    private static final String HAS_ADMIN_ROLE = "testAdmin";
    private static final String HAS_TEACHER_ROLE = "testTeacher";
    private static final String HAS_STUDENT_ROLE = "testStudent";
    private static final long TEST_ID_FOR_MODEL = 1L;
    private Group testGroup;
    private GroupDto testGroupDto;

    @BeforeEach
    void init() {
        testGroup = new Group();
        testGroupDto = new GroupDto();
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_getAllGroups_shouldPassWithAuthorizedUser() throws Exception {
        when(groupService.getAllGroups()).thenReturn(TEST_GROUPS);

        mockMvc.perform(get("/group")).andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", Matchers.hasSize(1)))
                .andExpect(model().attribute("groups", Matchers.contains(TEST_GROUPS.get(0))));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_getAllGroups_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        groupController.getAllGroups(model);

        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    void testGroupController_getAllGroups_shouldPassInCaseUnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/group"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_createGroupForm_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/group/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("group-create"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testGroupController_createGroupForm_shouldNotPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/group/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testGroupController_createGroupForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/group/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_createGroup_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(post("/group")
                .flashAttr("group", testGroupDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/group"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_createGroup_shouldCallMockOneTime() throws Exception {
        groupController.createGroup(testGroupDto);

        verify(groupService, times(1)).saveGroup(any(Group.class));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_updateGroup_shouldPassForAdminRole() throws Exception {
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/group/{id}", TEST_ID_FOR_MODEL)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/group"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_updateGroup_shouldCallMockOneTime() throws Exception {
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        groupController.updateGroup(anyLong(), testGroupDto);

        verify(groupService, times(1)).getGroupById(anyLong());
        verify(groupService, times(1)).updateGroup(any(Group.class), anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_deleteGroup_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/group/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/group"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_deleteGroup_shouldCallMockOneTime() throws Exception {
        groupController.deleteGroup(anyLong());

        verify(groupService, times(1)).deleteGroup(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testGroupController_deleteGroup_shouldPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/group/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testGroupController_deleteGroup_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/group/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_updateGroupForm_shouldPassForAdminRole() throws Exception {
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        mockMvc.perform(get("/group/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("group-update"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testGroupController_updateGroupForm_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        groupController.updateGroupForm(anyLong(), model);

        verify(groupService, times(1)).getGroupById(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testGroupController_updateGroupForm_shouldPassForTeacherRole() throws Exception {
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        mockMvc.perform(get("/group/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("group-update"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testGroupController_updateGroupForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/group/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

}
