package com.example.springsecurityproject.repository;
import com.example.springsecurityproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUsersByUsername(String username);
}
