package com.example.springsecurityproject.jwt;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class if made only to handle the authentication.
 */
@NoArgsConstructor
@Data
public class UsernameAndPasswordAuthenticationRequest {
    private String username;
    private String password;
}
