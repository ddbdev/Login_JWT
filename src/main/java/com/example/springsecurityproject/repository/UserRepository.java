package com.example.springsecurityproject.repository;
import com.example.springsecurityproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * This is the repository of UserEntity that extends JpaRepository
 */

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity getReferenceById(Long id);
    UserEntity findUsersByUsername(String username);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="update users set confirm_token = NULL, confirmed_at = NOW(), is_enabled = 1 WHERE confirm_token = ?1", nativeQuery=true)
    void setConfirmTokenToNull(String token);
    UserEntity findUserByConfirmToken(String token);
}
