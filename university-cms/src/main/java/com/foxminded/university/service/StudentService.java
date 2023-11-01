package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Student;

public interface StudentService {

    Student saveStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(long studentId);

    Student updateStudent(Student student, long studentId);

    void deleteStudent(long studentId);

}
