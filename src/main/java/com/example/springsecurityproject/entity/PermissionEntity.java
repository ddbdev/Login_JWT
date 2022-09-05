package com.example.springsecurityproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This is the permission entity, it'll create a database table with 3 columns.
 * The id column is auto incremented for every new insert
 * The permission column is the specified permission for that role
 * The role_entity_id it's the role associated for that permission
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission", nullable = false)
    private String permissionName;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoleEntity roleEntity;
}
