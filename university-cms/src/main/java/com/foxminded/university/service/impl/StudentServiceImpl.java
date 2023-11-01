package com.foxminded.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Student;
import com.foxminded.university.repository.StudentRepository;
import com.foxminded.university.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private static final String MESSAGE = "Record under provided id - not exist";

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new ServiceException(MESSAGE));
    }

    @Override
    public Student updateStudent(Student student, long studentId) {
        Student existingStudent = getStudentById(studentId);
        existingStudent.setFirstname(student.getFirstname());
        existingStudent.setLastname(student.getLastname());

        studentRepository.save(existingStudent);
        return existingStudent;
    }

    @Override
    public void deleteStudent(long studentId) {
        studentRepository.deleteById(studentId);

    }

}
