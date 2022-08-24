package com.example.springsecurityproject.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {

    private final Integer studentId;
    private final String studentName;


}
