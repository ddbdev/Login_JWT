package com.example.springsecurityproject.controllerGET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class TemplateController {

    @GetMapping(path = "/")
    public String getLogin(){
        return "index";
    }

    @GetMapping(path = "/courses")
    public String getCourses(){
        return "courses";
    }
}
