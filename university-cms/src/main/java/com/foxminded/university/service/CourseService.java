package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

public interface CourseService {

    Course saveCourse(Course course);

    List<Course> getAllCourses();

    Course getCourseById(long courseId);

    Course updateCourse(Course courseEntity, long courseId);

    void deleteCourse(long courseId);

    Optional<Course> findByTeacher(Teacher teacher);

    void removeTeacherFromCourse(Teacher teacher);

}
