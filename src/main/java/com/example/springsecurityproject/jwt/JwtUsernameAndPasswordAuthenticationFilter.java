package com.example.springsecurityproject.jwt;

import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;

/**
 * This is our AuthenticationFilter, that will be executed at the login.
 * If the user exists it'll authenticate it and return a JWT with every requested claims for it.
 */
@AllArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private TokenService tokenService;
    private AuthenticationManager authenticationManager;

    /**
     * This method is executed - as the name says - when a client is trying to log-in via /login
     * It'll return an Authentication based if the user exists, if it doesn't exist, nothing will happen.
     * If the user exists and authentication success it'll be forwarded to the next method, successfulAuthentication();
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return Authentication
     * @throws AuthenticationException Authentication
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            UsernameAndPasswordAuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Once the authentication has been redirected here the method will create a new JWT with the details of the authenticated user.
     * This method also add into the token_entity table the secret key encrypted in base64 so every user has a different key,
     * that will guarantee a major security since if a hacker steal somehow a secret key it'll be only for that specific user and not
     * for all users.
     * After that it'll add headers to the response:
     * - Authenticated : "AuthString" + username in base 64
     * - Authorization : "Bearer" + the jwt token just created
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param chain FilterChain
     * @param authResult Authentication
     * @throws IOException Exception
     * @throws ServletException Exception
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretKey = Encoders.BASE64.encode(key.getEncoded());
        tokenService.addToken(secretKey, (UserEntity) authResult.getPrincipal());
        String JWTtoken = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new java.util.Date())
                .setExpiration(Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()))
                .compact();

        String username = authResult.getName();
        String encryptedUser = Encoders.BASE64.encode(username.getBytes());
        response.addHeader("Authenticated","AuthString" + encryptedUser);
        response.addHeader("Authorization", "Bearer" + JWTtoken);

    }
}
