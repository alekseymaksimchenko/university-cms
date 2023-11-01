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
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.web.dto.StudentDto;

@WebMvcTest(StudentController.class)
@Import({ SecurityConfig.class, TestUserDetailsService.class })
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @MockBean
    private GroupService groupService;

    private static final List<Student> TEST_STUDENTS = new ArrayList<>(Arrays.asList(new Student()));
    private static final String HAS_ADMIN_ROLE = "testAdmin";
    private static final String HAS_TEACHER_ROLE = "testTeacher";
    private static final String HAS_STUDENT_ROLE = "testStudent";
    private static final String TEST_ID_FOR_DTO = "1";
    private static final long TEST_ID_FOR_MODEL = 1L;

    private Student testStudent;
    private StudentDto testStudentDto;

    @BeforeEach
    void init() {
        testStudent = new Student();
        testStudentDto = new StudentDto();
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_getAllStudents_shouldPassWithAuthorizedUser() throws Exception {
        when(studentService.getAllStudents()).thenReturn(TEST_STUDENTS);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(view().name("student"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", Matchers.hasSize(1)))
                .andExpect(model().attribute("students", Matchers.contains(TEST_STUDENTS.get(0))));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_getAllStudents_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        studentController.getAllStudents(model);

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testStudentController_getAllStudents_shouldNotPassInCaseUnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/student"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_createStudentForm_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/student/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-create"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testStudentController_createStudentForm_shouldNotPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/student/new"))
                  .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testStudentController_createStudentForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/student/new"))
                   .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_createStudent_shouldPassForAdminRole() throws Exception {
        Group testGroup = new Group();
        testStudentDto.setGroupId(TEST_ID_FOR_DTO);

        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/student")
                .flashAttr("student", testStudentDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_createStudent_shouldCallMockOneTime() throws Exception {
        Group testGroup = new Group();
        testStudentDto.setGroupId(TEST_ID_FOR_DTO);

        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        studentController.createStudent(testStudentDto);

        verify(groupService, times(1)).getGroupById(anyLong());
        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_updateStudent_shouldPassForAdminRole() throws Exception {
        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);

        mockMvc.perform(post("/student/{id}", TEST_ID_FOR_MODEL)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_updateStudent_shouldCallMockOneTime() throws Exception {
        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);

        studentController.updateStudent(anyLong(), testStudentDto);

        verify(studentService, times(1)).getStudentById(anyLong());
        verify(studentService, times(1)).updateStudent(any(Student.class), anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_deleteStudent_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/student/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_deleteStudent_shouldCallMockOneTime() throws Exception {
        studentController.deleteStudent(anyLong());

        verify(studentService, times(1)).deleteStudent(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testStudentController_deleteStudent_shouldPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/student/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testStudentController_deleteStudent_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/student/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_updateStudentForm_shouldPassForAdminRole() throws Exception {
        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);

        mockMvc.perform(get("/student/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("student-update"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_updateStudentForm_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        studentController.updateStudentForm(anyLong(), model);

        verify(studentService, times(1)).getStudentById(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testStudentController_updateStudentForm_shouldPassForTeacherRole() throws Exception {
        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);

        mockMvc.perform(get("/student/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testStudentController_updateStudentForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/student/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testStudentController_changeStudentGroupForm_shouldPassForTeacherRole() throws Exception {
        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);

        mockMvc.perform(get("/student/student-group-update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("student-group-update"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testStudentController_changeStudentGroupForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/student/student-group-update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_changeStudentGroup_shouldPassForAdminRole() throws Exception {
        Group testGroup = new Group();
        testGroup.addStudentToGroup(testStudent);
        testStudent.setGroup(testGroup);
        testStudentDto.setGroupId(TEST_ID_FOR_DTO);

        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/student/student-group-update/{id}", TEST_ID_FOR_MODEL)
                .flashAttr("student", testStudentDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testStudentController_changeStudentGroup_shouldCallMockRightTimesInRightOrder() throws Exception {
        Group group = new Group();
        testStudent.setGroup(group);
        testStudentDto.setGroupId(TEST_ID_FOR_DTO);

        when(studentService.getStudentById(anyLong())).thenReturn(testStudent);
        when(groupService.getGroupById(anyLong())).thenReturn(group);

        studentController.changeStudentGroup(anyLong(), testStudentDto);

        verify(studentService, times(1)).getStudentById(anyLong());
        verify(groupService, times(2)).getGroupById(anyLong());
        verify(groupService, times(2)).updateGroup(any(Group.class), anyLong());
        verify(studentService, times(1)).updateStudent(any(Student.class), anyLong());

        InOrder inOrder = Mockito.inOrder(studentService, groupService);
        inOrder.verify(studentService).getStudentById(anyLong());
        inOrder.verify(groupService).getGroupById(anyLong());
        inOrder.verify(groupService).updateGroup(any(Group.class), anyLong());
        inOrder.verify(groupService).getGroupById(anyLong());
        inOrder.verify(groupService).updateGroup(any(Group.class), anyLong());
        inOrder.verify(studentService).updateStudent(any(Student.class), anyLong());
    }

}
