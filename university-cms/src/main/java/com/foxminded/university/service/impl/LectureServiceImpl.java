package com.foxminded.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.repository.LectureRepository;
import com.foxminded.university.service.LectureService;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private static final String MESSAGE = "Record under provided id - not exist";

    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public Lecture saveLecture(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    @Override
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @Override
    public Lecture getLectureById(long lectureId) {
        return lectureRepository.findById(lectureId).orElseThrow(() -> new ServiceException(MESSAGE));
    }

    @Override
    public Lecture updateLecture(Lecture lecture, long lectureId) {
        Lecture existingLecture = getLectureById(lectureId);
        existingLecture.setLectureDate(lecture.getLectureDate());
        existingLecture.setLectureTime(lecture.getLectureTime());

        lectureRepository.save(existingLecture);
        return existingLecture;
    }

    @Override
    public void deleteLecture(long lectureId) {
        lectureRepository.deleteById(lectureId);
    }

    @Override
    public List<Lecture> findAllLecturesOrderByDateAscTimeAsc() {
        return lectureRepository.findAllLecturesOrderByDateAscTimeAsc();
    }

    @Override
    public List<Lecture> findAllLecturesOnDateOrderByDateAscTimeAsc(String date) {
        return lectureRepository.findAllLecturesOnDateOrderByDateAscTimeAsc(date);
    }

    @Override
    public List<Lecture> findAllLecturesBetweenDatesOrderByDateAscTimeAsc(String startDate, String endDate) {
        return lectureRepository.findAllLecturesBetweenDatesOrderByDateAscTimeAsc(startDate, endDate);
    }

}
