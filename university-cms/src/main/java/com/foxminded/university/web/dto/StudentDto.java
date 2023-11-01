package com.foxminded.university.web.dto;



public class StudentDto {

    private String firstName;
    private String lastName;
    private String groupId;

    public StudentDto() {

    }

    public StudentDto(String firstName, String lastName, String groupId) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
