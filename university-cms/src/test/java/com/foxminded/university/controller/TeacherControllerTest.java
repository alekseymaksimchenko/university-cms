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
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.foxminded.university.config.SecurityConfig;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.web.dto.TeacherDto;

@WebMvcTest(TeacherController.class)
@Import({ SecurityConfig.class, TestUserDetailsService.class })
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherController teacherController;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CourseService courseService;

    private static final List<Teacher> TEST_TEACHERS = new ArrayList<>(Arrays.asList(new Teacher()));
    private static final String HAS_ADMIN_ROLE = "testAdmin";
    private static final String HAS_TEACHER_ROLE = "testTeacher";
    private static final String HAS_STUDENT_ROLE = "testStudent";
    private static final String TEST_ID_FOR_DTO = "1";
    private static final long TEST_ID_FOR_MODEL = 1L;

    private Teacher testTeacher;
    private TeacherDto testTeacherDto;

    @BeforeEach
    void init() {
        testTeacher = new Teacher();
        testTeacherDto = new TeacherDto();
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_getAllTeachers_shouldPassWithAuthorizedUser() throws Exception {
        when(teacherService.getAllTeachers()).thenReturn(TEST_TEACHERS);

        mockMvc.perform(get("/teacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attribute("teachers", Matchers.hasSize(1)))
                .andExpect(model().attribute("teachers", Matchers.contains(TEST_TEACHERS.get(0))));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_getAllTeachers_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        teacherController.getAllTeachers(model);

        verify(teacherService, times(1)).getAllTeachers();
    }

    @Test
    void testTeacherController_getAllTeachers_shouldNotPassInCaseUnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/teacher"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_createTeacherForm_shouldshouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/teacher/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-create"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testTeacherController_createTeacherForm_shouldNotPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/teacher/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testTeacherController_createTeacherForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/teacher/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_createTeacher_shouldPassForAdminRole() throws Exception {
        Course testCourse = new Course();
        testTeacherDto.setCourseId(TEST_ID_FOR_DTO);

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);

        mockMvc.perform(post("/teacher")
                .flashAttr("teacher", testTeacherDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_createTeacher_shouldCallMockOneTimeInRightOrder() throws Exception {
        Course testCourse = new Course();
        testTeacherDto.setCourseId(TEST_ID_FOR_DTO);

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);

        teacherController.createTeacher(testTeacherDto);

        verify(courseService, times(1)).getCourseById(anyLong());
        verify(courseService, times(1)).updateCourse(any(Course.class), anyLong());
        verify(teacherService, times(1)).saveTeacher(any(Teacher.class));

        InOrder inOrder = Mockito.inOrder(courseService, teacherService);
        inOrder.verify(courseService).getCourseById(anyLong());
        inOrder.verify(courseService).updateCourse(any(Course.class), anyLong());
        inOrder.verify(teacherService).saveTeacher(any(Teacher.class));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_updateTeacher_shouldPassForAdminRole() throws Exception {
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        mockMvc.perform(post("/teacher/{id}", TEST_ID_FOR_MODEL)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_updateTeacher_shouldCallMockOneTime() throws Exception {
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        teacherController.updateTeacher(anyLong(), testTeacherDto);

        verify(teacherService, times(1)).getTeacherById(anyLong());
        verify(teacherService, times(1)).updateTeacher(any(Teacher.class), anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_deleteTeacher_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/teacher/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_deleteTeacher_shouldCallMockOneTime() throws Exception {
        teacherController.deleteTeacher(anyLong());

        verify(teacherService, times(1)).deleteTeacher(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testTeacherController_deleteTeacher_shouldPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/teacher/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testTeacherController_deleteTeacher_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/teacher/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_updateTeacherForm_shouldPassForAdminRole() throws Exception {
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        mockMvc.perform(get("/teacher/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-update"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_updateTeacherForm_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        teacherController.updateTeacherForm(anyLong(), model);

        verify(teacherService, times(1)).getTeacherById(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testTeacherController_updateTeacherForm_shouldPassForTeacherRole() throws Exception {
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        mockMvc.perform(get("/teacher/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-update"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testTeacherController_updateTeacherForm_shouldNotPassForStudentRole() throws Exception {
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        mockMvc.perform(get("/teacher/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_removeCourseFromTeacher_shouldPassForAdminRole() throws Exception {
        Course testCourse = new Course();

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        mockMvc.perform(get("/teacher/remove-course/{id}", TEST_ID_FOR_MODEL))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testTeacherController_removeCourseFromTeacher_shouldCallMocksOneTimeInRightOrder() throws Exception {
        Course testCourse = new Course();

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);
        teacherController.removeCourseFromTeacher(anyLong());

        verify(courseService, times(1)).getCourseById(anyLong());
        verify(teacherService, times(1)).getTeacherById(anyLong());
        verify(teacherService, times(1)).updateTeacher(any(Teacher.class), anyLong());
        verify(courseService, times(1)).updateCourse(any(Course.class), anyLong());

        InOrder inOrder = Mockito.inOrder(teacherService, courseService);
        inOrder.verify(courseService).getCourseById(anyLong());
        inOrder.verify(teacherService).getTeacherById(anyLong());
        inOrder.verify(courseService).updateCourse(any(Course.class), anyLong());
        inOrder.verify(teacherService).updateTeacher(any(Teacher.class), anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testTeacherController_removeCourseFromTeacher_shouldPassForTeacherRole() throws Exception {
        Course testCourse = new Course();

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);
        when(teacherService.getTeacherById(anyLong())).thenReturn(testTeacher);

        mockMvc.perform(get("/teacher/remove-course/{id}", TEST_ID_FOR_MODEL))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/teacher"));
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testTeacherController_removeCourseFromTeacher_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/teacher/remove-course/{id}", TEST_ID_FOR_MODEL))
                    .andExpect(status().isForbidden());
    }

}
