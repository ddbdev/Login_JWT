package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.entity.UserPermissionEntity;
import com.example.springsecurityproject.repository.RoleRepository;
import com.example.springsecurityproject.repository.UserPermissionRepository;
import com.example.springsecurityproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is the service of UserPermissionEntity
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserPermissionService {


    private final UserPermissionRepository userPermissionRepository;

    /**
     * This method will add a specified role to a specified user.
     * e.g. roleId: 1
     *      userId: 1
     * Will add to user with id 1 the role with id 1
     *
     * @param role RoleEntity
     * @param user UserEntity
     */
    public void addRoleToUser(RoleEntity role, UserEntity user){

        UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
        userPermissionEntity.setUser(user);
        userPermissionEntity.setRole(role);

        userPermissionRepository.save(userPermissionEntity);
    }

    /**
     * This method returns a List of string where those strings are the "ROLE_ADMIN" , "ROLE_MODERATOR" roles.
     * @param userEntity UserEntity
     * @return List<String>
     */
    public List<String> getRolesByUser(UserEntity userEntity){

        return userPermissionRepository.getUserPermissionEntity(userEntity);
    }

    /**
     * This method will return a list of string with the ID's of the previous role
     * @param user UserEntity
     * @return List<String>
     */
    public List<String> getRoleId(UserEntity user){
        return userPermissionRepository.getUserPermissionId(user);
    }

    /** This method will return a list of string containing the roles and the permission of that specified user
     * @param ids String
     * @return List<String>
     */
    public List<String> getUserPermissions (String ids){
        return userPermissionRepository.getPermissionNameBySet(ids);
    }

    /**
     * This method will convert the List of String into a set of GrantedAuthorities
     * @param roles List<String>
     * @return Set<SimpleGrantedAuthority>
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(List<String> roles){
        Set<SimpleGrantedAuthority> permissions = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        for (String p : roles)
        {
            permissions.add(new SimpleGrantedAuthority(p));
        }

        return permissions;
    }

    /**
     * This method will set to the user a set of GrantedAuthorities (See UserEntity -> authorities)
     * @param authorities Set<SimpleGrantedAuthority>
     * @param userEntity UserEntity
     */
    public void setGrantedAuthorities (Set<SimpleGrantedAuthority> authorities, UserEntity userEntity){
        userEntity.setAuthorities(authorities);
    }
}
