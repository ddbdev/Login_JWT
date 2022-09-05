package com.example.springsecurityproject.controller;
import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.service.RoleService;
import com.example.springsecurityproject.service.SelectForUserRole;
import com.example.springsecurityproject.service.UserPermissionService;
import com.example.springsecurityproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


/** This is the UserController
 * Over here we're injecting some depenencies, the first one is UserService, that's returning all the function in UserRepository
 * same thing for the RoleService and UserPermissionService.
 */
@RestController
@AllArgsConstructor
@Slf4j
public class UserController {


    /** Dependency Injection */
    @Autowired
    private final UserService userService;
    private final RoleService roleService;
    private final UserPermissionService userPermissionService;


    /**
     * The main role of @registerUser is as the name says to register a user.
     * We're requesting the parameter username and password, the method check if a user with the same username exists
     * and if it's found the request will return a response with the "User already found in the database" as a ResponseEntity.
     * Otherwise, it'll create a new user with the username and password passed before, meanwhile It will be created a token
     * that will be returned with the URI "confirm?token=", by clicking on that you'll be redirected on a new page, that will confirm
     * that user and will enable it to access.
     * @param user String
     * @param password String
     * @return ResponseEntity<?>
     */
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

    /**
     * This method confirms a registered user, it'll return a ResponseEntity based on some conditions based on a URI "confirm?token="
     * If you make a request with an empty token it'll return an "Url not valid" response
     * If you make a request with a wrong token it'll return a "Token doesn't exist" response
     * If you make a request with a correct token it'll return a "User confirmed" response
     *
     * The .findToken() method returns a row if a user has that token in the "confirm_token" column in the database
     * @param token String
     * @return ResponseEntity
     */
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){

        if (token.isEmpty())
        {
            return new ResponseEntity<>("Url not valid", HttpStatus.BAD_REQUEST);
        }
        if (!userService.findToken(token))
        {
            return new ResponseEntity<>("Token doesn't exist", HttpStatus.BAD_REQUEST);
        }
        else{
            userService.setToken(token);
            return new ResponseEntity<>("User confirmed", HttpStatus.CREATED);
        }

    }


    /**
     * This method allows a user with a specific role (watch ApplicationSecurityConfig.java for more) to send a request
     * to set a specific role to a specific user.
     * e.g. of the request (Postman):
     * {
     *     "userId": 1
     *     "roleId : 1
     * }
     * That will return a ResponseEntity (if conditions are fulfilled) saying that the role with id 1 has been setted to
     * user with id 1.
     * @param submit JSON
     * @return ResponseEntity
     */
    @PostMapping("/admin/permission")
    public ResponseEntity<String> setPermissionToUser(@RequestBody SelectForUserRole submit){
        UserEntity user = userService.findUserById(submit.getUserId());
        RoleEntity role = roleService.findById(submit.getRoleId());

        userPermissionService.addRoleToUser(role, user);
        return ResponseEntity.ok().body("Role "+ role.getRoleName() +" added to " + user.getUsername());
    }

}
