package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUsersByUsername(username);
        if (user == null){
            throw new NullPointerException("User not found");
            //TODO Gestire l'eccezione.
        }
        return user;
    }

    public UserEntity addUser(UserEntity userEntity){
        String encodedPassword = this.passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        return userRepository.save(userEntity);
    }

}
