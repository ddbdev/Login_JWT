package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.TokenEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * This is the service of UserEntity
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPermissionService userPermissionService;


    /**
     * This method is going to load a user if it exists and convert it into a UserDetails.
     * @param username String
     * @return UserDetails
     * @throws UsernameNotFoundException Exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUsersByUsername(username);

        if (user == null){
            throw new NullPointerException("User not found");
            //TODO Gestire l'eccezione.
        }

        List<String> roleId = userPermissionService.getRoleId(user);
        StringBuilder rolesId = new StringBuilder();

        for (String str : roleId){
            rolesId.append(str).append(",");
        }
        if (rolesId.length() > 0)
            rolesId.replace(rolesId.lastIndexOf(","), rolesId.lastIndexOf(",") + 1, "");

        List<String> auth = userPermissionService.getRolesByUser(user);
        auth.addAll(userPermissionService.getUserPermissions(rolesId.toString()));

        Set<SimpleGrantedAuthority> authorities = userPermissionService.getGrantedAuthorities(auth);
        userPermissionService.setGrantedAuthorities(authorities, user);

        return user;
    }

    /**
     * This method when invoked will save into the Database a new user, with an encrypted password (PasswordConfig.java for more info)
     * @param userEntity UserEntity
     * @return UserEntity
     */
    public UserEntity addUser(UserEntity userEntity){
        String encodedPassword = this.passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        return userRepository.save(userEntity);
    }

    /**
     * This method is invoked when the user is sending a request into "/confirm?token=" and will enable that user.
     * @param token String
     */
    public void setToken(String token){
        userRepository.setConfirmTokenToNull(token);
    }

    /**
     * This method will return true if a token exists, or false if a token doesn't
     * @param token String
     * @return boolean
     */
    public boolean findToken(String token){
        UserEntity user = userRepository.findUserByConfirmToken(token);
        return user != null;
    }

    /**
     * This method will find a user by passing the id of it.
     * @param id Long
     * @return UserEntity
     */
    public UserEntity findUserById(Long id){
        return userRepository.getReferenceById(id);
    }
}
