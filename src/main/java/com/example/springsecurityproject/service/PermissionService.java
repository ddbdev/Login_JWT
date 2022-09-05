package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.PermissionEntity;
import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.repository.PermissionRepository;
import com.example.springsecurityproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This is the service of PermissionEntity
 */
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    /**
     * This method will add a permission to a role
     * e.g. permission:write - ROLE_ADMIN
     * e.g  permission:read - ROLE_MODERATOR
     *
     * @param permission String
     * @param role RoleEntity
     */
    public void addPermissionToRoles(String permission, RoleEntity role)
    {
        RoleEntity roleEntity = roleRepository.getReferenceById(role.getId());
        if (roleEntity == null)
            throw new IllegalStateException("User not found");
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionName(permission);
        permissionEntity.setRoleEntity(roleEntity);
        permissionRepository.save(permissionEntity);

    }
}
