package com.example.springsecurityproject.controller;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


@RestController
@AllArgsConstructor
@Slf4j
public class UserController {


    @Autowired
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("username") String user,
            @RequestParam("password") String password
    ){
        try {
            if (userService.loadUserByUsername(user) != null){
                URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/register").toUriString());
                return ResponseEntity.created(uri).body("User already found in the database");
            }
            else {
                throw new NullPointerException();
            }

        }
        catch (NullPointerException e)
        {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user);
            newUser.setPassword(password);
            String confirmToken = newUser.getConfirmToken();
            String path = "/confirm?token=" + confirmToken;
            userService.addUser(newUser);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(path).toUriString().replace("%3F", "?"));
            return ResponseEntity.ok().body("User registered, enable your account here: \n" + uri +"");

        }

    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){

        if (token.isEmpty())
        {
            return new ResponseEntity<>("Url non valido", HttpStatus.BAD_REQUEST);
        }
        if (userService.findToken(token) == "Il token non esiste")
        {
            return new ResponseEntity<>("Il token non esiste", HttpStatus.BAD_REQUEST);
        }
        else{
            userService.setToken(token);
            return new ResponseEntity<>("Token confermato", HttpStatus.CREATED);
        }

    }

}
