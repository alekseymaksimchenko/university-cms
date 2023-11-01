package com.foxminded.university.web.dto;

public class CourseDto {

    private String name;
    private String teacherId;

    public CourseDto() {
    }

    public CourseDto(String name, String teacherId) {
        super();
        this.name = name;
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "CourseDto [name=" + name + ", teacherId=" + teacherId + "]";
    }

}
