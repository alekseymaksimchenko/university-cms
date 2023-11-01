package com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foxminded.university.model.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query(value = "FROM #{#entityName} ORDER BY lectureDate ASC, lectureTime ASC")
    List<Lecture> findAllLecturesOrderByDateAscTimeAsc();

    @Query(value = "FROM #{#entityName} WHERE lectureDate = ?1 ORDER BY lectureDate ASC, lectureTime ASC")
    List<Lecture> findAllLecturesOnDateOrderByDateAscTimeAsc(String date);

    @Query(value = "FROM #{#entityName} WHERE lectureDate >= ?1 AND lectureDate < ?2 ORDER BY lectureDate ASC, lectureTime ASC")
    List<Lecture> findAllLecturesBetweenDatesOrderByDateAscTimeAsc(String startDate, String endDate);

}