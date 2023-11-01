package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.repository.CourseRepository;
import com.foxminded.university.service.impl.CourseServiceImpl;

@SpringBootTest(classes = CourseServiceImpl.class)
class CourseServiceImplTest {

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private Course testCourse;

    @MockBean
    private Teacher testTeacher;

    @Autowired
    private CourseServiceImpl courseServiceImpl;

    private static final String MESSAGE = "Record under provided id - not exist";
    private static final long testId = 1L;

    @Test
    void testCourseServiceImp_saveShouldPass() {
        assertAll(() -> courseServiceImpl.saveCourse(testCourse));
    }

    @Test
    void testCourseServiceImp_saveShouldCallRepositoryOneTime() {
        courseServiceImpl.saveCourse(testCourse);
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void testCourseServiceImp_getAllShouldPass() {
        assertAll(() -> courseServiceImpl.getAllCourses());
    }

    @Test
    void testCourseServiceImp_getAllShouldCallRepositoryOneTime() {
        courseServiceImpl.getAllCourses();
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testCourseServiceImp_findByIdShouldPass() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findById(testId)).thenReturn(course);
        assertAll(() -> courseServiceImpl.getCourseById(testId));
    }

    @Test
    void testCourseServiceImp_findByIdShouldCallRepositoryOneTime() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findById(testId)).thenReturn(course);
        courseServiceImpl.getCourseById(testId);
        verify(courseRepository, times(1)).findById(testId);
    }

    @Test
    void testCourseServiceImp_findByIdShouldReturnServiceException_inCasoOfNull() {
        Exception exception = assertThrows(ServiceException.class, () -> courseServiceImpl.getCourseById(testId));
        String actual = exception.getMessage();
        String expected = MESSAGE;
        assertEquals(expected, actual);
    }

    @Test
    void testCourseServiceImp_updateShouldPass() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findById(testId)).thenReturn(course);
        assertAll(() -> courseServiceImpl.updateCourse(testCourse, testId));
    }

    @Test
    void testCourseServiceImp_updateShouldCallRepositoryOneTime() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findById(testId)).thenReturn(course);
        courseServiceImpl.updateCourse(testCourse, testId);
        verify(courseRepository, times(1)).findById(testId);
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void testCourseServiceImp_deleteShouldPass() {
        assertAll(() -> courseServiceImpl.deleteCourse(testId));
    }

    @Test
    void testCourseServiceImp_deleteShouldCallRepositoryOneTime() {
        courseServiceImpl.deleteCourse(testId);
        verify(courseRepository, times(1)).deleteById(testId);
    }

    @Test
    void testCourseServiceImp_findByTeacherShouldPass() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findByTeacher(testTeacher)).thenReturn(course);
        assertNotNull(courseServiceImpl.findByTeacher(testTeacher));
    }

    @Test
    void testCourseServiceImp_findByTeacherShouldCallRepositoryOneTime() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findByTeacher(testTeacher)).thenReturn(course);

        courseServiceImpl.findByTeacher(testTeacher);
        verify(courseRepository, times(1)).findByTeacher(testTeacher);
    }

    @Test
    void testCourseServiceImp_removeTeacherFromCourseShouldCallRepositoryTwoTimseInrightOrder() {
        Optional<Course> course = Optional.of(testCourse);
        when(courseRepository.findByTeacher(testTeacher)).thenReturn(course);

        courseServiceImpl.removeTeacherFromCourse(testTeacher);
        verify(courseRepository, times(1)).findByTeacher(testTeacher);
        verify(courseRepository, times(1)).save(testCourse);

        InOrder inOrder = Mockito.inOrder(courseRepository);
        inOrder.verify(courseRepository).findByTeacher(testTeacher);
        inOrder.verify(courseRepository).save(testCourse);
    }

}
