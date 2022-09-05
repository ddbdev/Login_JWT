package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * This is the service of the Role Entity
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * This method will add a role, without setting any permission (to set the permission read PermissionService.java)
     * e.g.
     *      id 1
     *      role_name ROLE_ADMIN
     *
     * @param roleName String
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> addRole(String roleName){
        RoleEntity role = new RoleEntity();
        role.setRoleName(roleName);
        roleRepository.save(role);

        return ResponseEntity.ok().body("Ruolo aggiunto");
    }

    /**
     * This will return a RoleEntity by passing the id of a role.
     * @param id Long
     * @return RoleEntity
     */
    public RoleEntity findById(Long id){
        return roleRepository.getReferenceById(id);
    }
}
