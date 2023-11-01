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

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.web.dto.StudentDto;

@Controller
public class StudentController {

    private static final String STUDENT_ATTRIBUTE = "student";
    private static final String STUDENTS_ATTRIBUTE = "students";
    private static final String STUDENT_DTO_ATTRIBUTE = "studentDto";
    private static final String HAS_ANY_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER')";
    private static final String HAS_STUFF_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')";
    private static final String HAS_ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        super();
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @ModelAttribute(STUDENT_DTO_ATTRIBUTE)
    public StudentDto studentDto() {
        return new StudentDto();
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/student")
    public String getAllStudents(Model model) {
        List<Student> sortedByIdStudentList = studentService.getAllStudents()
                .stream()
                .sorted(Comparator.comparingLong(Student::getId))
                .collect(Collectors.toList());
        model.addAttribute(STUDENTS_ATTRIBUTE, sortedByIdStudentList);
        return "student";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/student/new")
    public String createStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute(STUDENT_ATTRIBUTE, student);
        return "student-create";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @PostMapping("/student")
    public String createStudent(@ModelAttribute (STUDENT_ATTRIBUTE) StudentDto studentDto) {
        Student newStudent = new Student();
        newStudent.setFirstname(studentDto.getFirstName());
        newStudent.setLastname(studentDto.getLastName());

        Long groupId = Long.parseLong(studentDto.getGroupId());
        Group existingGroup = groupService.getGroupById(groupId);
        existingGroup.addStudentToGroup(newStudent);
        groupService.updateGroup(existingGroup, existingGroup.getId());

        newStudent.setGroup(existingGroup);

        studentService.saveStudent(newStudent);
        return "redirect:/student";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/student/update/{id}")
    public String updateStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute(STUDENT_ATTRIBUTE, studentService.getStudentById(id));
        return "student-update";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/student/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute(STUDENT_ATTRIBUTE) StudentDto studentDto) {
        Student existingStudent = studentService.getStudentById(id);
        existingStudent.setFirstname(studentDto.getFirstName());
        existingStudent.setLastname(studentDto.getLastName());
        studentService.updateStudent(existingStudent, existingStudent.getId());
        return "redirect:/student";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/student/student-group-update/{id}")
    public String changeStudentGroupForm(@PathVariable Long id, Model model) {
        model.addAttribute(STUDENT_ATTRIBUTE, studentService.getStudentById(id));
        return "student-group-update";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/student/student-group-update/{id}")
    public String changeStudentGroup(@PathVariable Long id, @ModelAttribute(STUDENT_ATTRIBUTE) StudentDto studentDto) {
        Student existingStudent = studentService.getStudentById(id);

        Group existingGroup = groupService.getGroupById(existingStudent.getGroup().getId());
        existingGroup.removeStudentFromGroup(existingStudent);
        groupService.updateGroup(existingGroup, existingGroup.getId());

        Long groupId = Long.parseLong(studentDto.getGroupId());
        Group newGroup = groupService.getGroupById(groupId);
        newGroup.addStudentToGroup(existingStudent);
        groupService.updateGroup(newGroup, newGroup.getId());

        existingStudent.setGroup(newGroup);

        studentService.updateStudent(existingStudent, existingStudent.getId());
        return "redirect:/student";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/student";
    }

}
