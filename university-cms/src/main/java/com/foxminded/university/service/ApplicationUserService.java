package com.foxminded.university.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.foxminded.university.model.ApplicationUser;
import com.foxminded.university.web.dto.UserRegistrationDto;

public interface ApplicationUserService extends UserDetailsService {

    ApplicationUser saveUser(UserRegistrationDto userRegistrationDto);

    List<ApplicationUser> getAllApplicationUsers();

    ApplicationUser addRole(String newRole, String username);

    ApplicationUser removeRole(String roleToRemove, String username);

    void deleteApplicationUser(String username);

}
