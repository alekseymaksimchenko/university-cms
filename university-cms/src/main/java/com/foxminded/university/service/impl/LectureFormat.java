package com.foxminded.university.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.Formattable;
import com.foxminded.university.web.dto.LectureDto;

@Service
public class LectureFormat implements Formattable {

    @Override
    public List<LectureDto> getStandardFormat(List<Lecture> lectures) {
        List<LectureDto> formattedLectures = new ArrayList<>();

        lectures.forEach(lecture -> {
            String lectureDate = lecture.getLectureDate();
            String lectureTime = lecture.getLectureTime();
            String groupName = lecture.getGroup().getName();
            String courseName = lecture.getCourse().getName();

            LectureDto lectureDto = new LectureDto();
            lectureDto.setId(lecture.getId());
            lectureDto.setLectureDate(lectureDate);
            lectureDto.setLectureTime(lectureTime);
            lectureDto.setGroupName(groupName);
            lectureDto.setCourseName(courseName);
            formattedLectures.add(lectureDto);
        });
        return formattedLectures;
    }

}
