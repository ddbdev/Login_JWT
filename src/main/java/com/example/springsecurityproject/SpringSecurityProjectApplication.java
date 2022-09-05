package com.example.springsecurityproject;

import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.RoleRepository;
import com.example.springsecurityproject.repository.UserRepository;
import com.example.springsecurityproject.service.PermissionService;
import com.example.springsecurityproject.service.RoleService;
import com.example.springsecurityproject.service.UserPermissionService;
import com.example.springsecurityproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityProjectApplication.class, args);
    }

    /**
     * This is executed when you start the program, it will add automatically some new records on the db.
     * It adds some roles and some permission to that roles.
     *
     * If you change the spring.jpa.hibernate.ddl-auto=create-drop to spring.jpa.hibernate.ddl-auto=update you can delete this method
     * after you start it once, otherwise it'll add this records everytime you start
     * @param roleService RoleService
     * @param permissionService PermissionService
     * @return
     */
    @Bean
    CommandLineRunner run(RoleService roleService, PermissionService permissionService){

        final String READ = "blog:read";
        final String WRITE = "blog:write";
        return args -> {

            roleService.addRole("ROLE_ADMIN");
            roleService.addRole("ROLE_MODERATOR");
            roleService.addRole("ROLE_USER");

            permissionService.addPermissionToRoles(READ, roleService.findById(1L));
            permissionService.addPermissionToRoles(WRITE,   roleService.findById(1L));
            permissionService.addPermissionToRoles(READ,  roleService.findById(2L));



        };
    }


}
