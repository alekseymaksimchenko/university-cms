package com.foxminded.university.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.web.dto.GroupDto;

@Controller
public class GroupController {

    private static final String GROUP_ATTRIBUTE = "group";
    private static final String GROUPS_ATTRIBUTE = "groups";
    private static final String GROUP_DTO_ATTRIBUTE = "groupDto";
    private static final String HAS_STUFF_ROLE = "hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER')";
    private static final String HAS_ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        super();
        this.groupService = groupService;
    }

    @ModelAttribute(GROUP_DTO_ATTRIBUTE)
    public GroupDto groupDto() {
        return new GroupDto();
    }

    @GetMapping("/group")
    public String getAllGroups(Model model) {
        model.addAttribute(GROUPS_ATTRIBUTE, groupService.getAllGroups());
        return "group";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/group/new")
    public String createGroupForm(Model model) {
        Group group = new Group();
        model.addAttribute(GROUP_ATTRIBUTE, group);
        return "group-create";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @PostMapping("/group")
    public String createGroup(@ModelAttribute(GROUP_ATTRIBUTE) GroupDto groupDto) {
        Group newGroup = new Group();
        newGroup.setName(groupDto.getGroupName());
        groupService.saveGroup(newGroup);
        return "redirect:/group";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @GetMapping("/group/update/{id}")
    public String updateGroupForm(@PathVariable Long id, Model model) {
        model.addAttribute(GROUP_ATTRIBUTE, groupService.getGroupById(id));
        return "group-update";
    }

    @PreAuthorize(HAS_STUFF_ROLE)
    @PostMapping("/group/{id}")
    public String updateGroup(@PathVariable Long id, @ModelAttribute(GROUP_ATTRIBUTE) GroupDto groupDto) {
        Group existingGroup = groupService.getGroupById(id);
        existingGroup.setName(groupDto.getGroupName());
        groupService.updateGroup(existingGroup, existingGroup.getId());
        return "redirect:/group";
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @GetMapping("/group/delete/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return "redirect:/group";
    }

}
