package com.foxminded.university.web.dto;

public class TeacherDto {

    private String firstName;
    private String lastName;
    private String course;

    public TeacherDto() {
        super();
    }

    public TeacherDto(String firstName, String lastName, String course) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourseId() {
        return course;
    }

    public void setCourseId(String course) {
        this.course = course;
    }

}
