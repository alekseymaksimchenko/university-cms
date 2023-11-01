package com.foxminded.university.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAutority(String autority);

}
