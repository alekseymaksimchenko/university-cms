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
import com.foxminded.university.web.dto.CourseDto;

@WebMvcTest(CourseController.class)
@Import({ SecurityConfig.class, TestUserDetailsService.class })

class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseController courseController;

    @MockBean
    private CourseService courseService;

    @MockBean
    private TeacherService teacherService;

    private static final List<Course> TEST_COURSES = new ArrayList<>(Arrays.asList(new Course()));
    private static final String HAS_ADMIN_ROLE = "testAdmin";
    private static final String HAS_TEACHER_ROLE = "testTeacher";
    private static final String HAS_STUDENT_ROLE = "testStudent";
    private static final String TEST_ID_FOR_DTO = "1";
    private static final long TEST_ID_FOR_MODEL = 1L;
    private Course testCourse;
    private CourseDto courseDto;
    Model model = new ConcurrentModel();

    @BeforeEach
    void init() {
        testCourse = new Course();
        courseDto = new CourseDto();
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_showAllCourses_shouldPassWithAuthorizedUser() throws Exception {
        when(courseService.getAllCourses()).thenReturn(TEST_COURSES);

        mockMvc.perform(get("/course"))
                .andExpect(status().isOk())
                .andExpect(view().name("course"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("courses", Matchers.hasSize(1)))
                .andExpect(model().attribute("courses", Matchers.contains(TEST_COURSES.get(0))));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_showAllCourses_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        courseController.showAllCourses(model);

        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void testCourseController_showAllCourses_shouldNotPassInCaseUnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/course"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_createCourseForm_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/course/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("course-create"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testCourseController_createCourseForm_shouldNotPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/course/new"))
                  .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testCourseController_createCourseForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/course/new"))
                   .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_createCourse_shouldPassForAdminRole() throws Exception {
        Teacher teacher = new Teacher();
        courseDto.setTeacherId(TEST_ID_FOR_DTO);

        when(teacherService.getTeacherById(anyLong())).thenReturn(teacher);

        mockMvc.perform(post("/course")
                .flashAttr("course", courseDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_createCourse_shouldCallMockOneTimeInRightOrder() throws Exception {
        Teacher teacher = new Teacher();
        courseDto.setTeacherId(TEST_ID_FOR_DTO);

        when(teacherService.getTeacherById(anyLong())).thenReturn(teacher);

        courseController.createCourse(courseDto);

        verify(teacherService, times(1)).getTeacherById(anyLong());
        verify(courseService, times(1)).removeTeacherFromCourse(teacher);
        verify(courseService, times(1)).saveCourse(testCourse);

        InOrder inOrder = Mockito.inOrder(teacherService, courseService);
        inOrder.verify(teacherService).getTeacherById(anyLong());
        inOrder.verify(courseService).removeTeacherFromCourse(any());
        inOrder.verify(courseService).saveCourse(any());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_updateCourse_shouldPassForAdminRole() throws Exception {
        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);

        mockMvc.perform(post("/course/{id}", TEST_ID_FOR_MODEL)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_updateCourse_shouldCallMockOneTime() throws Exception {
        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);

        courseController.updateCourse(TEST_ID_FOR_MODEL, courseDto);

        verify(courseService, times(1)).getCourseById(anyLong());
        verify(courseService, times(1)).updateCourse(any(Course.class), anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_deleteCourse_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/course/delete/{id}", TEST_ID_FOR_MODEL)).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_deleteCourse_shouldCallMockOneTime() throws Exception {
        courseController.deleteCourse(anyLong());

        verify(courseService, times(1)).deleteCourse(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testCourseController_deleteCourse_shouldPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/course/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testCourseController_deleteCourse_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/course/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_removeTeacherFromCourse_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/course/remove-teacher/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_removeTeacherFromCourse_shouldCallMockOneTimeInRightOrder() throws Exception {
        courseController.removeTeacherFromCourse(anyLong());

        verify(teacherService, times(1)).getTeacherById(anyLong());
        verify(courseService, times(1)).removeTeacherFromCourse(any());


        InOrder inOrder = Mockito.inOrder(teacherService, courseService);
        inOrder.verify(teacherService).getTeacherById(anyLong());
        inOrder.verify(courseService).removeTeacherFromCourse(any());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testCourseController_removeTeacherFromCourse_shouldPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/course/remove-teacher/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"));
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testCourseController_removeTeacherFromCourse_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/course/remove-teacher/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_updateCourseForm_shouldPassForAdminRole() throws Exception {
        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);

        mockMvc.perform(get("/course/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("course-update"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testCourseController_updateCourseForm_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        courseController.updateCourseForm(anyLong(), model);

        verify(courseService, times(1)).getCourseById(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testCourseController_updateCourseForm_shouldPassForTeacherRole() throws Exception {
        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);

        mockMvc.perform(get("/course/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("course-update"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testCourseController_updateCourseForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/course/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

}
