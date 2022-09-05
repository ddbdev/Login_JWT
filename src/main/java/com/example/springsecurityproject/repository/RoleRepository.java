package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * This is the repository of RoleEntity that extends JpaRepository
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Override
    RoleEntity getReferenceById(Long aLong);
}
