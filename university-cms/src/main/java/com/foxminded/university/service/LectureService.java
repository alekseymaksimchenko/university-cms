package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Lecture;

public interface LectureService {

    Lecture saveLecture(Lecture lecture);

    List<Lecture> getAllLectures();

    Lecture getLectureById(long lectureId);

    Lecture updateLecture(Lecture lecture, long lectureId);

    void deleteLecture(long lectureId);

    List<Lecture> findAllLecturesOrderByDateAscTimeAsc();

    List<Lecture> findAllLecturesOnDateOrderByDateAscTimeAsc(String date);

    List<Lecture> findAllLecturesBetweenDatesOrderByDateAscTimeAsc(String startDate, String endDate);

}
