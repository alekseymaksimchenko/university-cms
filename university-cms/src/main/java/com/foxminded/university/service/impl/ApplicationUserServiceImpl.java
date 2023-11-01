package com.foxminded.university.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foxminded.university.model.ApplicationUser;
import com.foxminded.university.model.Role;
import com.foxminded.university.repository.ApplicationUserRepository;
import com.foxminded.university.service.ApplicationUserService;
import com.foxminded.university.web.dto.UserRegistrationDto;

@Service
public class ApplicationUserServiceImpl implements UserDetailsService, ApplicationUserService {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserRepository applicationUserRepository;
    private static final String USER_NOT_FOUND = "Invalid Username or Password.";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    public ApplicationUserServiceImpl(PasswordEncoder passwordEncoder,
            ApplicationUserRepository applicationUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public ApplicationUser saveUser(UserRegistrationDto userRegistrationDto) {
        LOGGER.debug("Service Layer saveUser ({}) - userRegistrationDto from Controller ", userRegistrationDto);

        ApplicationUser applicationUser = new ApplicationUser(userRegistrationDto.getUserName(),
                passwordEncoder.encode(userRegistrationDto.getPassword()),
                new HashSet<>(Arrays.asList(new Role(userRegistrationDto.getRole()))));

        LOGGER.debug("Service Layer saveUser ({}) - DB applicationUser according to dto", applicationUser);
        return applicationUserRepository.save(applicationUser);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        ApplicationUser applicationUser = mapToUser(username);

        return new User(applicationUser.getUsername(), applicationUser.getPassword(),
                mapRolesToAutorities(applicationUser.getAutorities()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAutorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAutority())).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationUser> getAllApplicationUsers() {
        return applicationUserRepository.findAll();
    }

    @Override
    public void deleteApplicationUser(String username) {
        ApplicationUser applicationUser = mapToUser(username);
        applicationUserRepository.deleteById(applicationUser.getId());
        LOGGER.debug("Service Layer, ApplicationUser ({}) - was deleted", applicationUser);
    }

    @Override
    public ApplicationUser addRole(String newRole, String username) {
        ApplicationUser existingUser = mapToUser(username);
        Role role = new Role(newRole);
        existingUser.getAutorities().add(role);
        existingUser = applicationUserRepository.save(existingUser);
        LOGGER.debug("Service Layer, role ({}) was added to ApplicationUser ({})", newRole, existingUser);

        return existingUser;
    }

    @Override
    public ApplicationUser removeRole(String roleName, String username) {
        ApplicationUser existingUser = mapToUser(username);
        Role roleToremove = new Role(roleName);
        existingUser.getAutorities().remove(roleToremove);
        existingUser = applicationUserRepository.save(existingUser);
        LOGGER.debug("Service Layer, role ({}) was removed from ApplicationUser ({})", roleName, existingUser);

        return existingUser;
    }

    private ApplicationUser mapToUser(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

    }

}
