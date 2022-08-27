package com.example.springsecurityproject.jwt;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UsernameAndPasswordAuthenticationRequest {
    private String username;
    private String password;
}
