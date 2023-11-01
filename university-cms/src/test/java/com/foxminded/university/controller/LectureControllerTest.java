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
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.impl.DateParser;
import com.foxminded.university.service.impl.LectureFormat;
import com.foxminded.university.web.dto.LectureDto;

@WebMvcTest(LectureController.class)
@Import({ SecurityConfig.class, TestUserDetailsService.class })
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LectureController lectureController;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private DateParser dateParser;

    @MockBean
    private LectureFormat lectureFormat;

    @MockBean
    private CourseService courseService;

    @MockBean
    private GroupService groupService;

    private Lecture testLecture;
    private LectureDto testLectureDto;

    private static final String HAS_ADMIN_ROLE = "testAdmin";
    private static final String HAS_TEACHER_ROLE = "testTeacher";
    private static final String HAS_STUDENT_ROLE = "testStudent";
    private static final long TEST_ID_FOR_MODEL = 1L;

    @BeforeEach
    void init() {
        testLecture = new Lecture();
        testLectureDto = new LectureDto();
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_showTodayLectures_shouldPass() throws Exception {
        mockMvc.perform(get("/lecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture"))
                .andExpect(model().attributeExists("lecturesToday"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_showWeekLectures_shouldPass() throws Exception {
        mockMvc.perform(get("/lecture/week"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture-show-week"))
                .andExpect(model().attributeExists("lecturesWeek"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_showMonthLectures_shouldPass() throws Exception {
        mockMvc.perform(get("/lecture/month"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture-show-month"))
                .andExpect(model().attributeExists("lecturesMonth"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_showAllLectures_shouldPass() throws Exception {
        mockMvc.perform(get("/lecture/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture-show-all"))
                .andExpect(model().attributeExists("allLectures"));
    }

    @Test
    void testLectureController_showTodayLectures_shouldNotPassInCaseUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/lecture"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_createLectureForm_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/lecture/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture-create"))
                .andExpect(model().attributeExists("lecture"));
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testLectureController_createLectureForm_shouldNotPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/lecture/new"))
                  .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testLectureController_createLectureForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/lecture/new"))
                   .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_createLecture_shouldPassForAdminRole() throws Exception {
        Group testGroup = new Group();
        Course testCourse = new Course();
        
        testLectureDto.setCourseId("1");
        testLectureDto.setGroupId("1");

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);

        mockMvc.perform(post("/lecture")
                .flashAttr("lectureDto", testLectureDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecture"));
    }


    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_createLecture_shouldCallMockOneTimeInRightOrder_inCaseOfDtoContainsNotNullFields() {
        Group testGroup = new Group();
        Course testCourse = new Course();

        testLectureDto.setCourseId("1");
        testLectureDto.setGroupId("1");

        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);
        when(lectureService.saveLecture(any(Lecture.class))).thenReturn(testLecture);

        lectureController.createLecture(testLectureDto);

        verify(courseService, times(1)).getCourseById(anyLong());
        verify(courseService, times(1)).updateCourse(any(Course.class), anyLong());
        verify(groupService, times(1)).getGroupById(anyLong());
        verify(groupService, times(1)).updateGroup(any(Group.class), anyLong());
        verify(lectureService, times(1)).saveLecture(any(Lecture.class));

        InOrder inOreder = Mockito.inOrder(courseService, groupService, lectureService);
        inOreder.verify(courseService).getCourseById(anyLong());
        inOreder.verify(courseService).updateCourse(any(Course.class), anyLong());
        inOreder.verify(groupService).getGroupById(anyLong());
        inOreder.verify(groupService).updateGroup(any(Group.class), anyLong());
        inOreder.verify(lectureService).saveLecture(any(Lecture.class));
        inOreder.verifyNoMoreInteractions();
    }


    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_updateLecture_shouldPassForAdminRole() throws Exception {
        testLectureDto.setCourseId("1");
        testLectureDto.setGroupId("1");
        when(lectureService.getLectureById(anyLong())).thenReturn(testLecture);

        mockMvc.perform(post("/lecture/{id}", TEST_ID_FOR_MODEL)
                .flashAttr("lecture", testLectureDto)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecture"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_updateLecture_shouldCallMockOneTimeInRightOrder_inCaseOfDtoContainsNotNullFields() {
        Group testGroup = new Group();
        Course testCourse = new Course();

        testLectureDto.setCourseId("1");
        testLectureDto.setGroupId("1");
        when(lectureService.getLectureById(anyLong())).thenReturn(testLecture);
        when(courseService.getCourseById(anyLong())).thenReturn(testCourse);
        when(groupService.getGroupById(anyLong())).thenReturn(testGroup);
        when(lectureService.updateLecture(any(Lecture.class), anyLong())).thenReturn(testLecture);

        lectureController.updateLecture(anyLong(), testLectureDto);

        verify(lectureService, times(1)).getLectureById(anyLong());
        verify(courseService, times(1)).getCourseById(anyLong());
        verify(groupService, times(1)).getGroupById(anyLong());
        verify(lectureService, times(1)).updateLecture(any(Lecture.class), anyLong());

        InOrder inOreder = Mockito.inOrder(courseService, groupService, lectureService);
        inOreder.verify(lectureService).getLectureById(anyLong());
        inOreder.verify(courseService).getCourseById(anyLong());
        inOreder.verify(groupService).getGroupById(anyLong());
        inOreder.verify(lectureService).updateLecture(any(Lecture.class), anyLong());
        inOreder.verifyNoMoreInteractions();
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_deleteLecture_shouldPassForAdminRole() throws Exception {
        mockMvc.perform(get("/lecture/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecture"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_deleteLecture_shouldCallMockOneTime() throws Exception {
        lectureController.deleteLecture(anyLong());

        verify(lectureService, times(1)).deleteLecture(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testLectureController_deleteLecture_shouldPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/lecture/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testLectureController_deleteLecture_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/lecture/delete/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_updateLectureForm_shouldPassForAdminRole() throws Exception {
        when(lectureService.getLectureById(anyLong())).thenReturn(testLecture);

        mockMvc.perform(get("/lecture/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isOk())
                .andExpect(view().name("lecture-update"))
                .andExpect(model().attributeExists("lecture"));
    }

    @Test
    @WithUserDetails(value = HAS_ADMIN_ROLE)
    void testLectureController_updateLectureForm_shouldCallMockOneTime() throws Exception {
        Model model = new ConcurrentModel();
        lectureController.updateLectureForm(anyLong(), model);

        verify(lectureService, times(1)).getLectureById(anyLong());
    }

    @Test
    @WithUserDetails(value = HAS_TEACHER_ROLE)
    void testLectureController_updateLectureForm_shouldNotPassForTeacherRole() throws Exception {
        mockMvc.perform(get("/lecture/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = HAS_STUDENT_ROLE)
    void testLectureController_updateLectureForm_shouldNotPassForStudentRole() throws Exception {
        mockMvc.perform(get("/lecture/update/{id}", TEST_ID_FOR_MODEL))
                .andExpect(status().isForbidden());
    }

}
