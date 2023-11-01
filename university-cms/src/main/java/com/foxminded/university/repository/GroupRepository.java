package com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
