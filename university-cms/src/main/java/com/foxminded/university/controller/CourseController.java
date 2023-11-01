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
import com.foxminded.university.web.dto.CourseDto;

@Controller
public class CourseController {

    private static final String COURSE_ATTRIBUTE = "course";
    private static final String COURSES_ATTRIBUTE = "courses";
    private static final String COURSE_DTO_ATTRIBUTE = "courseDto";
    private static final String HAS_ANY_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')";
    private static final String HAS_STUFF_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')";
    private static final String HAS_ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
    private final CourseService courseService;
    private final TeacherService teacherService;

    public CourseController(CourseService courseService, TeacherService teacherService) {
        super();
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @ModelAttribute(COURSE_DTO_ATTRIBUTE)
    public CourseDto courseDto() {
        return new CourseDto();
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/course")
    public String showAllCourses(Model model) {
        List<Course> sortedByIdCourseList = courseService.getAllCourses().stream()
                .sorted(Comparator.comparingLong(Course::getId)).collect(Collectors.toList());
        model.addAttribute(COURSES_ATTRIBUTE, sortedByIdCourseList);
        return "course";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/course/new")
    public String createCourseForm(Model model) {
        Course course = new Course();
        model.addAttribute(COURSE_ATTRIBUTE, course);
        return "course-create";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/course")
    public String createCourse(@ModelAttribute(COURSE_ATTRIBUTE) CourseDto courseDto) {
        Course newCourse = new Course();
        newCourse.setName(courseDto.getName());

        Long teacherId = Long.parseLong(courseDto.getTeacherId());
        Teacher existingTeacher = teacherService.getTeacherById(teacherId);
        courseService.removeTeacherFromCourse(existingTeacher);

        existingTeacher.setCourse(newCourse);
        newCourse.setTeacher(existingTeacher);

        courseService.saveCourse(newCourse);
        return "redirect:/course";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/course/update/{id}")
    public String updateCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute(COURSE_ATTRIBUTE, courseService.getCourseById(id));
        return "course-update";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/course/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute(COURSE_ATTRIBUTE) CourseDto courseDto) {
        Course existingCourse = courseService.getCourseById(id);
        existingCourse.setName(courseDto.getName());
        courseService.updateCourse(existingCourse, existingCourse.getId());
        return "redirect:/course";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/course";

    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/course/remove-teacher/{id}")
    public String removeTeacherFromCourse(@PathVariable Long id) {
        Teacher existingTeacher = teacherService.getTeacherById(id);
        courseService.removeTeacherFromCourse(existingTeacher);
        return "redirect:/course";
    }

}