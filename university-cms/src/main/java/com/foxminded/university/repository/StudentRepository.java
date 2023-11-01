package com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
