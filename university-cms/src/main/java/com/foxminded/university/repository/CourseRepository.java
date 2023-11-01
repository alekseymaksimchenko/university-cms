package com.foxminded.university.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByTeacher(Teacher teacher);

}
