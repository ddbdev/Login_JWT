package com.example.springsecurityproject.controllerGET;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Those methods are handled by Thymeleaf, it'll return a .html file with the name specified on the return value. */
@Controller
@RequestMapping(path = "/")
public class TemplateController {

    @GetMapping(path = "/")
    public String getLogin(){
        return "index";
    }

    @GetMapping(path = "/welcome")
    public String getCourses(){
        return "welcome";
    }
}
