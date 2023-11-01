package com.foxminded.university.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByUsername(String username);

}
