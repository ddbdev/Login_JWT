package com.example.springsecurityproject.controller;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
    private ResponseEntity<String> setHeaders(String response){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/register").toUriString());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(uri);
        responseHeaders.set("registration-response", response);
        return new ResponseEntity<>(response, responseHeaders, HttpStatus.CREATED);
    }
    @Autowired
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("username") String user,
            @RequestParam("password") String password
    ){
        try {
            if (userService.loadUserByUsername(user) != null){
                return setHeaders("Username already found in the database");
            }
            else {
                throw new NullPointerException();
            }

        }
        catch (NullPointerException e)
        {
            String path = "/confirm?token=";
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user);
            newUser.setPassword(password);
            String confirmToken = newUser.getConfirmToken();
            userService.addUser(newUser);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(path + confirmToken).toUriString());
            setHeaders("User registered");

            return ResponseEntity.ok().body("User registered, enable your account here: \n"+ uri +"");

        }

    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){

        if (token.isEmpty())
        {
            return new ResponseEntity<>("Url non valido", HttpStatus.BAD_REQUEST);
        }
        if (userService.findToken(token).isEmpty())
        {
            return new ResponseEntity<>("Il token non esiste", HttpStatus.BAD_REQUEST);
        }
        else{
            userService.setToken(token);
            return new ResponseEntity<>("Token confermato", HttpStatus.CREATED);
        }

    }

}
