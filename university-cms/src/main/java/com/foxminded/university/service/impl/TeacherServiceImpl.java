package com.foxminded.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.repository.TeacherRepository;
import com.foxminded.university.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private static final String MESSAGE = "Record under provided id - not exist";

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow(() -> new ServiceException(MESSAGE));
    }

    @Override
    public Teacher updateTeacher(Teacher teacher, long teacherId) {
        Teacher existingTeacher = getTeacherById(teacherId);
        existingTeacher.setFirstname(teacher.getFirstname());
        existingTeacher.setLastname(teacher.getLastname());

        teacherRepository.save(existingTeacher);
        return existingTeacher;
    }

    @Override
    public void deleteTeacher(long teacherId) {
        teacherRepository.deleteById(teacherId);

    }

}
