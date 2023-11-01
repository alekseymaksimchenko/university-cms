package com.foxminded.university.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.web.dto.TeacherDto;

@Controller
public class TeacherController {

    private static final String TEACHER_ATTRIBUTE = "teacher";
    private static final String TEACHERS_ATTRIBUTE = "teachers";
    private static final String TEACHER_DTO_ATTRIBUTE = "teacherDto";
    private static final String HAS_ANY_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')";
    private static final String HAS_STUFF_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')";
    private static final String HAS_ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
    private final TeacherService teacherService;
    private final CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        super();
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @ModelAttribute(TEACHER_DTO_ATTRIBUTE)
    public TeacherDto teacherDto() {
        return new TeacherDto();
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/teacher")
    public String getAllTeachers(Model model) {
        List<Teacher> sortedByIdTeacherList = teacherService.getAllTeachers().stream()
                .sorted(Comparator.comparingLong(Teacher::getId))
                .collect(Collectors.toList());
        model.addAttribute(TEACHERS_ATTRIBUTE, sortedByIdTeacherList);
        return "teacher";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/teacher/new")
    public String createTeacherFrom(Model model) {
        Teacher teacher = new Teacher();
        model.addAttribute(TEACHER_ATTRIBUTE, teacher);
        return "teacher-create";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/teacher")
    public String createTeacher(@ModelAttribute(TEACHER_ATTRIBUTE) TeacherDto teacherDto) {
        Teacher newTeacher = new Teacher();
        newTeacher.setFirstname(teacherDto.getFirstName());
        newTeacher.setLastname(teacherDto.getLastName());

        Long courseId = Long.parseLong(teacherDto.getCourseId());
        Course existingCourse = courseService.getCourseById(courseId);
        existingCourse.setTeacher(newTeacher);
        newTeacher.setCourse(existingCourse);

        courseService.updateCourse(existingCourse, courseId);
        teacherService.saveTeacher(newTeacher);

        return "redirect:/teacher";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/teacher/update/{id}")
    public String updateTeacherForm(@PathVariable Long id, Model model) {
        model.addAttribute(TEACHER_ATTRIBUTE, teacherService.getTeacherById(id));
        return "teacher-update";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/teacher/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute(TEACHER_ATTRIBUTE) TeacherDto teacherDto) {
        Teacher existingTeacher = teacherService.getTeacherById(id);
        existingTeacher.setFirstname(teacherDto.getFirstName());
        existingTeacher.setLastname(teacherDto.getLastName());
        teacherService.updateTeacher(existingTeacher, existingTeacher.getId());
        return "redirect:/teacher";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/teacher/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/teacher";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/teacher/remove-course/{id}")
    public String removeCourseFromTeacher(@PathVariable Long id) {
        Course existingCourse = courseService.getCourseById(id);
        existingCourse.setTeacher(null);

        Teacher existingTeacher = teacherService.getTeacherById(id);
        existingTeacher.setCourse(null);

        courseService.updateCourse(existingCourse, id);
        teacherService.updateTeacher(existingTeacher, id);

        return "redirect:/teacher";
    }

}
