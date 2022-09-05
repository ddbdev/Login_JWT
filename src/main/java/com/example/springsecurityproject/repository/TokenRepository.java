package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.entity.TokenEntity;
import com.example.springsecurityproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


/**
 * This is the repository of TokenEntity that extends JpaRepository
 */
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity getKeyByUser(UserEntity user);
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM token_entity WHERE user_id = ?1")
    void deleteByUser(UserEntity user);
}
