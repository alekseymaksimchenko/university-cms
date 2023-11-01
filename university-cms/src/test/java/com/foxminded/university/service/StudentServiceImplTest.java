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
import com.foxminded.university.model.Student;
import com.foxminded.university.repository.StudentRepository;
import com.foxminded.university.service.impl.StudentServiceImpl;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceImplTest {

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private Student testStudent;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    private static final String MESSAGE = "Record under provided id - not exist";
    private static final long testId = 1L;

    @Test
    void testStudentServiceImp_saveShouldPass() {
        assertAll(() -> studentServiceImpl.saveStudent(testStudent));
    }

    @Test
    void testStudentServiceImp_saveShouldCallRepositoryOneTime() {
        studentServiceImpl.saveStudent(testStudent);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    void testStudentServiceImp_getAllShouldPass() {
        assertAll(() -> studentServiceImpl.getAllStudents());
    }

    @Test
    void testStudentServiceImp_getAllShouldCallRepositoryOneTime() {
        studentServiceImpl.getAllStudents();
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testStudentServiceImp_findByIdShouldPass() {
        Optional<Student> student = Optional.of(testStudent);
        when(studentRepository.findById(testId)).thenReturn(student);
        assertAll(() -> studentServiceImpl.getStudentById(testId));
    }

    @Test
    void testStudentServiceImp_findByIdShouldCallRepositoryOneTime() {
        Optional<Student> student = Optional.of(testStudent);
        when(studentRepository.findById(testId)).thenReturn(student);
        studentServiceImpl.getStudentById(testId);
        verify(studentRepository, times(1)).findById(testId);
    }

    @Test
    void testStudentServiceImp_findByIdShouldReturnServiceException_inCasoOfNull() {
        Exception exception = assertThrows(ServiceException.class, () -> studentServiceImpl.getStudentById(testId));
        String actual = exception.getMessage();
        String expected = MESSAGE;
        assertEquals(expected, actual);
    }

    @Test
    void testStudentServiceImp_updateShouldPass() {
        Optional<Student> student = Optional.of(testStudent);
        when(studentRepository.findById(testId)).thenReturn(student);
        assertAll(() -> studentServiceImpl.updateStudent(testStudent, testId));
    }

    @Test
    void testStudentServiceImp_updateShouldCallRepositoryOneTime() {
        Optional<Student> student = Optional.of(testStudent);
        when(studentRepository.findById(testId)).thenReturn(student);
        studentServiceImpl.updateStudent(testStudent, testId);
        verify(studentRepository, times(1)).findById(testId);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    void testStudentServiceImp_deleteShouldPass() {
        assertAll(() -> studentServiceImpl.deleteStudent(testId));
    }

    @Test
    void testStudentServiceImp_deleteShouldCallRepositoryOneTime() {
        studentServiceImpl.deleteStudent(testId);
        verify(studentRepository, times(1)).deleteById(testId);
    }

}
