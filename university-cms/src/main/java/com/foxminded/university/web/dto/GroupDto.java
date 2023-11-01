package com.foxminded.university.web.dto;

public class GroupDto {

    private String groupName;

    public GroupDto(String groupName) {
        this.groupName = groupName;
    }

    public GroupDto() {

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "GroupDto [groupName=" + groupName + "]";
    }

}
