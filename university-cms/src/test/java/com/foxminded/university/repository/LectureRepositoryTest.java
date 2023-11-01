package com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.foxminded.university.model.Lecture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sample-data.sql",
        "/insert-lectures-script.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;

    private static final List<Lecture> lectureList = new ArrayList<>();
    static {
        lectureList.add(new Lecture("2023-10-04", "09:00 - 11:20"));
        lectureList.add(new Lecture("2023-10-04", "11:30 - 12:50"));
        lectureList.add(new Lecture("2023-10-04", "13:30 - 14:50"));
        lectureList.add(new Lecture("2023-10-04", "15:00 - 16:20"));
        lectureList.add(new Lecture("2023-10-05", "09:00 - 11:20"));
        lectureList.add(new Lecture("2023-10-05", "11:30 - 12:50"));
        lectureList.add(new Lecture("2023-10-05", "13:30 - 14:50"));
        lectureList.add(new Lecture("2023-10-05", "15:00 - 16:20"));
        lectureList.add(new Lecture("2023-10-15", "09:00 - 11:20"));
        lectureList.add(new Lecture("2023-10-15", "11:30 - 12:50"));
        lectureList.add(new Lecture("2023-10-22", "13:30 - 14:50"));
        lectureList.add(new Lecture("2023-10-22", "15:00 - 16:20"));
        lectureList.add(new Lecture("2023-11-01", "15:00 - 16:20"));
        lectureList.add(new Lecture("2023-11-31", "15:00 - 16:20"));
    }

    @Test
    void testLectureRepository_findAllLecturesOrderByDateAscTimeAsc_shouldReturnCorrectList() {
        List<Lecture> actual = lectureRepository.findAllLecturesOrderByDateAscTimeAsc();
        List<Lecture> expected = lectureRepository.findAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void testLectureRepository_findAllLecturesOrderByDateAscTimeAsc_shouldReturnListWithCorrectSortingByDateAndTime() {
        List<Lecture> actual = lectureRepository.findAllLecturesOrderByDateAscTimeAsc();
        AtomicInteger value = new AtomicInteger(0);

        actual.forEach(lecture -> {
            int counter = value.getAndIncrement();
            assertEquals(lectureList.get(counter).getLectureDate(), lecture.getLectureDate());
            assertEquals(lectureList.get(counter).getLectureTime(), lecture.getLectureTime());
        });
    }

    @Test
    void testLectureRepository_findAllLecturesOnDateOrderByDateAscTimeAsc_shouldReturnListWithCorrectSortingByDateAndTime() {
        String date = "2023-10-04";
        List<Lecture> actual = lectureRepository.findAllLecturesOnDateOrderByDateAscTimeAsc(date);
        AtomicInteger value = new AtomicInteger(0);

        actual.forEach(lecture -> {
            int counter = value.getAndIncrement();
            assertEquals(lectureList.get(counter).getLectureDate(), lecture.getLectureDate());
            assertEquals(lectureList.get(counter).getLectureTime(), lecture.getLectureTime());
        });
    }

    @Test
    void testLectureRepository_findAllLecturesBetweenDatesOrderByDateAscTimeAsc_shouldReturnListWithCorrectSortingByDateAndTime() {
        String startDate = "2023-10-04";
        String endDate = "2023-10-10";
        List<Lecture> actual = lectureRepository.findAllLecturesBetweenDatesOrderByDateAscTimeAsc(startDate, endDate);
        AtomicInteger value = new AtomicInteger(0);

        actual.forEach(lecture -> {
            int counter = value.getAndIncrement();
            assertEquals(lectureList.get(counter).getLectureDate(), lecture.getLectureDate());
            assertEquals(lectureList.get(counter).getLectureTime(), lecture.getLectureTime());
        });
    }

}
