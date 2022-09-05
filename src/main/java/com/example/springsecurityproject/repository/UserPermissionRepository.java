package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


/**
 * This is the repository of UserPermissionEntity that extends JpaRepository
 */
public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, Long> {

    //Questa ritorna ROLE ADMIN E ROLE MODERATOR
    @Query(value = "SELECT t.role_name FROM role_entity AS t INNER JOIN user_permission_entity AS t2 ON t2.role_id = t.id WHERE t2.user_id = ?1", nativeQuery = true)
    List<String> getUserPermissionEntity(UserEntity userEntity);

    //Questo ritorna gli id dei ruoli admin e mod
    @Query(value = "SELECT role_id FROM user_permission_entity WHERE user_id = ?1 ORDER BY role_id ASC", nativeQuery = true)
    List<String> getUserPermissionId(UserEntity user);

    //Questo ritorna i permessi associati a quel ruolo
    @Query(value = "SELECT DISTINCT t3.permission FROM permission_entity AS t3 WHERE FIND_IN_SET(t3.role_entity_id, :ids)", nativeQuery = true)
    List<String> getPermissionNameBySet(@Param("ids") String rolesId);
}
