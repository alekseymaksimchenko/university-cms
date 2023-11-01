package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Teacher;

public interface TeacherService {

    Teacher saveTeacher(Teacher teacher);

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(long teacherId);

    Teacher updateTeacher(Teacher courseEntity, long teacherId);

    void deleteTeacher(long teacherId);

}
