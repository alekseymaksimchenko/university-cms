package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.repository.TeacherRepository;
import com.foxminded.university.service.impl.TeacherServiceImpl;

@SpringBootTest(classes = TeacherServiceImpl.class)
class TeacherServiceImplTest {

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private Teacher testTeacher;

    @Autowired
    private TeacherServiceImpl teacherServiceImpl;

    private static final String MESSAGE = "Record under provided id - not exist";
    private static final long testId = 1L;

    @Test
    void testTeacherServiceImp_saveShouldPass() {
        assertAll(() -> teacherServiceImpl.saveTeacher(testTeacher));
    }

    @Test
    void testTeacherServiceImp_saveShouldCallRepositoryOneTime() {
        teacherServiceImpl.saveTeacher(testTeacher);
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    void testTeacherServiceImp_getAllShouldPass() {
        assertAll(() -> teacherServiceImpl.getAllTeachers());
    }

    @Test
    void testTeacherServiceImp_getAllShouldCallRepositoryOneTime() {
        teacherServiceImpl.getAllTeachers();
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testTeacherServiceImp_findByIdShouldPass() {
        Optional<Teacher> teacher = Optional.of(testTeacher);
        when(teacherRepository.findById(testId)).thenReturn(teacher);
        assertAll(() -> teacherServiceImpl.getTeacherById(testId));
    }

    @Test
    void testTeacherServiceImp_findByIdShouldCallRepositoryOneTime() {
        Optional<Teacher> teacher = Optional.of(testTeacher);
        when(teacherRepository.findById(testId)).thenReturn(teacher);
        teacherServiceImpl.getTeacherById(testId);
        verify(teacherRepository, times(1)).findById(testId);
    }

    @Test
    void testTeacherServiceImp_findByIdShouldReturnServiceException_inCasoOfNull() {
        Exception exception = assertThrows(ServiceException.class, () -> teacherServiceImpl.getTeacherById(testId));
        String actual = exception.getMessage();
        String expected = MESSAGE;
        assertEquals(expected, actual);
    }

    @Test
    void testTeacherServiceImp_updateShouldPass() {
        Optional<Teacher> teacher = Optional.of(testTeacher);
        when(teacherRepository.findById(testId)).thenReturn(teacher);
        assertAll(() -> teacherServiceImpl.updateTeacher(testTeacher, testId));
    }

    @Test
    void testTeacherServiceImp_updateShouldCallRepositoryOneTime() {
        Optional<Teacher> teacher = Optional.of(testTeacher);
        when(teacherRepository.findById(testId)).thenReturn(teacher);
        teacherServiceImpl.updateTeacher(testTeacher, testId);
        verify(teacherRepository, times(1)).findById(testId);
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    void testTeacherServiceImp_deleteShouldPass() {
        assertAll(() -> teacherServiceImpl.deleteTeacher(testId));
    }

    @Test
    void testTeacherServiceImp_deleteShouldCallRepositoryOneTime() {
        teacherServiceImpl.deleteTeacher(testId);
        verify(teacherRepository, times(1)).deleteById(testId);
    }

}
