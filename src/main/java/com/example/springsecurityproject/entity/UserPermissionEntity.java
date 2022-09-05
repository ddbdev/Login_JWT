package com.example.springsecurityproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/**
 * This entity will store each Main role to a specific user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private RoleEntity role;
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

}
