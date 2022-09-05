package com.example.springsecurityproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This is the Role Entity, it has only 2 columns
 * The first column is "id" and as always it's auto incremented for any new insertion
 * The second column is "role_name", e.g. ROLE_ADMIN, ROLE_MODERATOR etc...
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String roleName;
}
