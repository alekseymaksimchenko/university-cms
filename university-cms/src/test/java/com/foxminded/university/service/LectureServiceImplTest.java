package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.repository.LectureRepository;
import com.foxminded.university.service.impl.LectureServiceImpl;

@SpringBootTest(classes = LectureServiceImpl.class)
class LectureServiceImplTest {

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private Lecture testLecture;

    @Autowired
    private LectureServiceImpl lectureServiceImpl;

    private static final String MESSAGE = "Record under provided id - not exist";
    private static final long testId = 1L;

    @Test
    void testLectureServiceImp_saveShouldPass() {
        assertAll(() -> lectureServiceImpl.saveLecture(testLecture));
    }

    @Test
    void testLectureServiceImp_saveShouldCallRepositoryOneTime() {
        lectureServiceImpl.saveLecture(testLecture);
        verify(lectureRepository, times(1)).save(testLecture);
    }

    @Test
    void testLectureServiceImp_getAllShouldPass() {
        assertAll(() -> lectureServiceImpl.getAllLectures());
    }

    @Test
    void testLectureServiceImp_getAllShouldCallRepositoryOneTime() {
        lectureServiceImpl.getAllLectures();
        verify(lectureRepository, times(1)).findAll();
    }

    @Test
    void testLectureServiceImp_findByIdShouldPass() {
        Optional<Lecture> lecture = Optional.of(testLecture);
        when(lectureRepository.findById(testId)).thenReturn(lecture);
        assertAll(() -> lectureServiceImpl.getLectureById(testId));
    }

    @Test
    void testLectureServiceImp_findByIdShouldCallRepositoryOneTime() {
        Optional<Lecture> lecture = Optional.of(testLecture);
        when(lectureRepository.findById(testId)).thenReturn(lecture);
        lectureServiceImpl.getLectureById(testId);
        verify(lectureRepository, times(1)).findById(testId);
    }

    @Test
    void testLectureServiceImp_findByIdShouldReturnServiceException_inCasoOfNull() {
        Exception exception = assertThrows(ServiceException.class, () -> lectureServiceImpl.getLectureById(testId));
        String actual = exception.getMessage();
        String expected = MESSAGE;
        assertEquals(expected, actual);
    }

    @Test
    void testLectureServiceImp_updateShouldPass() {
        Optional<Lecture> lecture = Optional.of(testLecture);
        when(lectureRepository.findById(testId)).thenReturn(lecture);
        assertAll(() -> lectureServiceImpl.updateLecture(testLecture, testId));
    }

    @Test
    void testLectureServiceImp_updateShouldCallRepositoryOneTime() {
        Optional<Lecture> lecture = Optional.of(testLecture);
        when(lectureRepository.findById(testId)).thenReturn(lecture);
        lectureServiceImpl.updateLecture(testLecture, testId);
        verify(lectureRepository, times(1)).findById(testId);
        verify(lectureRepository, times(1)).save(testLecture);
    }

    @Test
    void testLectureServiceImp_deleteShouldPass() {
        assertAll(() -> lectureServiceImpl.deleteLecture(testId));
    }

    @Test
    void testLectureServiceImp_deleteShouldCallRepositoryOneTime() {
        lectureServiceImpl.deleteLecture(testId);
        verify(lectureRepository, times(1)).deleteById(testId);
    }

    @Test
    void testStudentServiceImp_findAllLecturesOrderByDateAscTimeAsc_ShouldPass() {
        assertAll(() -> lectureServiceImpl.findAllLecturesOrderByDateAscTimeAsc());
    }

    @Test
    void testStudentServiceImp_findAllLecturesOnDateOrderByDateAscTimeAsc_Pass() {
        assertAll(() -> lectureServiceImpl.findAllLecturesOnDateOrderByDateAscTimeAsc(anyString()));
    }

    @Test
    void testStudentServiceImp_findAllLecturesBetweenDatesOrderByDateAscTimeAsc_Pass() {
        assertAll(() -> lectureServiceImpl.findAllLecturesBetweenDatesOrderByDateAscTimeAsc(anyString(), anyString()));
    }

}
