package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.Group;
import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.service.impl.GroupServiceImpl;

@SpringBootTest(classes = GroupServiceImpl.class)
class GroupServiceImplTest {

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private Group testGroup;

    @Autowired
    private GroupServiceImpl groupServiceImpl;

    private static final String MESSAGE = "Record under provided id - not exist";
    private static final long testId = 1L;

    @Test
    void testGroupServiceImpl_saveShouldPass() {
        assertAll(() -> groupServiceImpl.saveGroup(testGroup));
    }

    @Test
    void testGroupServiceImpl_saveShouldCallRepositoryOneTime() {
        groupServiceImpl.saveGroup(testGroup);
        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    void testGroupServiceImpl_getAllShouldPass() {
        assertAll(() -> groupServiceImpl.getAllGroups());
    }

    @Test
    void testGroupServiceImpl_getAllShouldCallRepositoryOneTime() {
        groupServiceImpl.getAllGroups();
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testGroupServiceImpl_findByIdShouldPass() {
        Optional<Group> group = Optional.of(testGroup);
        when(groupRepository.findById(testId)).thenReturn(group);
        assertAll(() -> groupServiceImpl.getGroupById(testId));
    }

    @Test
    void testGroupServiceImpl_findByIdShouldCallRepositoryOneTime() {
        Optional<Group> group = Optional.of(testGroup);
        when(groupRepository.findById(testId)).thenReturn(group);
        groupServiceImpl.getGroupById(testId);
        verify(groupRepository, times(1)).findById(testId);
    }

    @Test
    void testGroupServiceImpl_findByIdShouldReturnServiceException_inCasoOfNull() {
        Exception exception = assertThrows(ServiceException.class, () -> groupServiceImpl.getGroupById(testId));
        String actual = exception.getMessage();
        String expected = MESSAGE;
        assertEquals(expected, actual);
    }

    @Test
    void testGroupServiceImpl_updateShouldPass() {
        Optional<Group> group = Optional.of(testGroup);
        when(groupRepository.findById(testId)).thenReturn(group);
        assertAll(() -> groupServiceImpl.updateGroup(testGroup, testId));
    }

    @Test
    void testGroupServiceImpl_updateShouldCallRepositoryOneTime() {
        Optional<Group> group = Optional.of(testGroup);
        when(groupRepository.findById(testId)).thenReturn(group);
        groupServiceImpl.updateGroup(testGroup, testId);
        verify(groupRepository, times(1)).findById(testId);
        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    void testGroupServiceImpl_deleteShouldPass() {
        assertAll(() -> groupServiceImpl.deleteGroup(testId));
    }

    @Test
    void testGroupServiceImpl_deleteShouldCallRepositoryOneTime() {
        groupServiceImpl.deleteGroup(testId);
        verify(groupRepository, times(1)).deleteById(testId);
    }
}
