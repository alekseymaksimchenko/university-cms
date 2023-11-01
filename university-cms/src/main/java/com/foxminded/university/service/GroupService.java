package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GroupService {

    Group saveGroup(Group group);

    List<Group> getAllGroups();

    Group getGroupById(long groupId);

    Group updateGroup(Group group, long groupId);

    void deleteGroup(long groupId);

}
