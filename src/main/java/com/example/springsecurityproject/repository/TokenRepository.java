package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.entity.TokenEntity;
import com.example.springsecurityproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity getKeyByUser(UserEntity user);
}
