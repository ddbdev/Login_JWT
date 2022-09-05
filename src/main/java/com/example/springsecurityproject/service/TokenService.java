package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.TokenEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * This is the service for the Token Entity
 */
@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    /**
     * This method will add the "token" (Secret key) for a specified user.
     * @param key String
     * @param user UserEntity
     * @return TokenEntity
     */
    public TokenEntity addToken(String key, UserEntity user){

        TokenEntity tokenCheck = tokenRepository.getKeyByUser(user);
        if (tokenCheck != null)
        {
            tokenRepository.deleteByUser(user);
        }
        TokenEntity token = new TokenEntity();
        token.setToken(key);
        token.setUser(user);
        return tokenRepository.save(token);
    }
}
