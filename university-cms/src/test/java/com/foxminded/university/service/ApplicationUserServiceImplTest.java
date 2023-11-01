package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.foxminded.university.model.ApplicationUser;
import com.foxminded.university.model.Role;
import com.foxminded.university.repository.ApplicationUserRepository;
import com.foxminded.university.service.impl.ApplicationUserServiceImpl;
import com.foxminded.university.web.dto.UserRegistrationDto;

@SpringBootTest(classes = ApplicationUserServiceImpl.class)
class ApplicationUserServiceImplTest {

    @Autowired
    private ApplicationUserServiceImpl applicationUserServiceImpl;

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private ApplicationUser testApplicationUser;
    private UserRegistrationDto testUserRegistrationDto;
    private Role testRoleAdmin;
    private static final String USERNAME = "userName";
    private static final String PASSWORD = "password";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private static final String RUNTIME_EXCEPTION = "Error";

    private static final String USER_NOT_FOUND = "Invalid Username or Password.";

    @BeforeEach
    void init() {
        testRoleAdmin = new Role(ROLE_ADMIN);
        testApplicationUser = new ApplicationUser(USERNAME, PASSWORD, new HashSet<>(Arrays.asList(testRoleAdmin)));
        testUserRegistrationDto = new UserRegistrationDto(USERNAME, PASSWORD, ROLE_ADMIN);
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(applicationUserRepository.save(testApplicationUser)).thenReturn(testApplicationUser);
        when(applicationUserRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(testApplicationUser));
    }

    @Test
    void testApplicationUserServiceImpl_saveUserShouldSaveUserToDb_accordingToUserDto() {

        ApplicationUser sevedUser = applicationUserServiceImpl.saveUser(testUserRegistrationDto);

        assertNotNull(sevedUser);
        assertEquals(testUserRegistrationDto.getUserName(), sevedUser.getUsername());
        assertEquals(testUserRegistrationDto.getPassword(), sevedUser.getPassword());
        assertEquals(testUserRegistrationDto.getRole(),
                sevedUser.getAutorities().stream().findFirst().get().getAutority());
    }

    @Test
    void testApplicationUserServiceImpl_saveUserShouldThrowRuntimeException_inCaseOfError() {
        when(applicationUserRepository.save(testApplicationUser)).thenThrow(new RuntimeException(RUNTIME_EXCEPTION));

        assertThrows(RuntimeException.class, () -> applicationUserServiceImpl.saveUser(testUserRegistrationDto));
    }

    @Test
    void testApplicationUserServiceImpl_saveShouldCallMocksOnceInRightOrder() {
        applicationUserServiceImpl.saveUser(testUserRegistrationDto);
        verify(applicationUserRepository, times(1)).save(testApplicationUser);
        verify(passwordEncoder, times(1)).encode(PASSWORD);

        InOrder inOrder = Mockito.inOrder(passwordEncoder, applicationUserRepository);
        inOrder.verify(passwordEncoder).encode(PASSWORD);
        inOrder.verify(applicationUserRepository).save(testApplicationUser);
    }

    @Test
    void testApplicationUserServiceImpl_loadUserByUsername_shouldReturnUserDetailsAccordingToDbUser() {
        UserDetails actual = applicationUserServiceImpl.loadUserByUsername(USERNAME);
        UserDetails expected = new User(USERNAME, PASSWORD, Arrays.asList(new SimpleGrantedAuthority(ROLE_ADMIN)));

        assertNotNull(actual);
        assertEquals(expected, actual);

    }

    @Test
    void testApplicationUserServiceImpl_loadUserByUsernameShouldThrowException_inCaseOfNotFindApplicationUser() {
        when(applicationUserRepository.findByUsername(USERNAME))
                .thenThrow(new UsernameNotFoundException(USER_NOT_FOUND));

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> applicationUserServiceImpl.loadUserByUsername(USERNAME));

        String expected = USER_NOT_FOUND;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void testApplicationUserServiceImpl_loadUserByUsernameShouldCallRepositoryOneTime() {
        applicationUserServiceImpl.loadUserByUsername(USERNAME);
        verify(applicationUserRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void testApplicationUserServiceImpl_getAllShouldCallRepositoryOneTime() {
        applicationUserServiceImpl.getAllApplicationUsers();
        verify(applicationUserRepository, times(1)).findAll();
    }

    @Test
    void testApplicationUserServiceImpl_addRoleToUserShouldPass() {
        applicationUserServiceImpl.saveUser(testUserRegistrationDto);
        Role testRoleUser = new Role(ROLE_USER);

        Set<Role> expected = new HashSet<>(Arrays.asList(testRoleAdmin, testRoleUser));

        Set<Role> actual = applicationUserServiceImpl.addRole(ROLE_USER, USERNAME).getAutorities();

        assertEquals(expected, actual);
    }

    @Test
    void testApplicationUserServiceImpl_addRoleToUserShouldCallMocksOnce() {
        applicationUserServiceImpl.addRole(ROLE_ADMIN, USERNAME);
        verify(applicationUserRepository, times(1)).findByUsername(USERNAME);
        verify(applicationUserRepository, times(1)).save(any(ApplicationUser.class));
    }

    @Test
    void testApplicationUserServiceImpl_removeRoleToUserShouldPass() {
        applicationUserServiceImpl.saveUser(testUserRegistrationDto);
        applicationUserServiceImpl.addRole(ROLE_USER, USERNAME);
        Role testRoleUser = new Role(ROLE_USER);

        Set<Role> expected = new HashSet<>(Arrays.asList(testRoleUser));

        Set<Role> actual = applicationUserServiceImpl.removeRole(ROLE_ADMIN, USERNAME).getAutorities();

        assertEquals(expected, actual);
    }

    @Test
    void testApplicationUserServiceImpl_removeRoleToUserShouldCallMocksOnce() {
        applicationUserServiceImpl.removeRole(ROLE_ADMIN, USERNAME);
        verify(applicationUserRepository, times(1)).findByUsername(USERNAME);
        verify(applicationUserRepository, times(1)).save(any(ApplicationUser.class));
    }

    @Test
    void testApplicationUserServiceImpl_deleteShouldCallRepositoryOneTime() {
        applicationUserServiceImpl.deleteApplicationUser(USERNAME);
        verify(applicationUserRepository, times(1)).findByUsername(USERNAME);
        verify(applicationUserRepository, times(1)).deleteById(anyLong());

        InOrder inOrder = Mockito.inOrder(applicationUserRepository);
        inOrder.verify(applicationUserRepository).findByUsername(USERNAME);
        inOrder.verify(applicationUserRepository).deleteById(anyLong());
    }

}
