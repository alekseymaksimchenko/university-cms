package com.foxminded.university.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.repository.CourseRepository;
import com.foxminded.university.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private static final String MESSAGE = "Record under provided id - not exist";

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new ServiceException(MESSAGE));
    }

    @Override
    public Course updateCourse(Course course, long courseId) {
        Course existingCourse = getCourseById(courseId);
        existingCourse.setName(course.getName());

        courseRepository.save(existingCourse);
        return existingCourse;
    }

    @Override
    public void deleteCourse(long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public Optional<Course> findByTeacher(Teacher teacher) {
        return courseRepository.findByTeacher(teacher);
    }

    @Override
    public void removeTeacherFromCourse(Teacher teacher) {
        Course existingCourse = findByTeacher(teacher).orElse(null);

        if (existingCourse != null) {
            existingCourse.setTeacher(null);
            saveCourse(existingCourse);
        }
    }

}
