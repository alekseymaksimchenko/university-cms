package com.foxminded.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Group;
import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private static final String MESSAGE = "Record under provided id - not exist";

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group getGroupById(long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new ServiceException(MESSAGE));
    }

    @Override
    public Group updateGroup(Group group, long groupId) {
        Group existingGroup = getGroupById(groupId);
        existingGroup.setName(group.getName());

        groupRepository.save(existingGroup);
        return existingGroup;
    }

    @Override
    public void deleteGroup(long groupId) {
        groupRepository.deleteById(groupId);

    }

}
