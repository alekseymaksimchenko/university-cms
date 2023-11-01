package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.impl.LectureFormat;
import com.foxminded.university.web.dto.LectureDto;

class LectureFormatTest {

    private LectureFormat lectureFormat;

    private static final List<Lecture> lectureList;

    static {
        lectureList = new ArrayList<>();
        Lecture testLecture = new Lecture("2023-10-04", "09:00 - 11:20");
        Course testCourse = new Course("testCourseName");
        Group testGroup = new Group("testGroupName");

        testLecture.setCourse(testCourse);
        testLecture.setGroup(testGroup);
        lectureList.add(testLecture);
    }

    @BeforeEach
    void init() {
        lectureFormat = new LectureFormat();
    }

    @Test
    void testLectureFormat_ee_ee() {
        List<LectureDto> testLectureDto = lectureFormat.getStandardFormat(lectureList);

        String expectedDate = "2023-10-04";
        String expectedTime = "09:00 - 11:20";
        String expectedGroup = "testGroupName";
        String expectedCourse = "testCourseName";

        String actualDate = testLectureDto.get(0).getLectureDate();
        String actualTime = testLectureDto.get(0).getLectureTime();
        String actualGroup = testLectureDto.get(0).getGroupName();
        String actualCourse = testLectureDto.get(0).getCourseName();

        assertEquals(expectedDate, actualDate);
        assertEquals(expectedTime, actualTime);
        assertEquals(expectedGroup, actualGroup);
        assertEquals(expectedCourse, actualCourse);
    }

}
