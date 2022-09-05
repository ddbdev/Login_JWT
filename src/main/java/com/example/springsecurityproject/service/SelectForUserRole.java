package com.example.springsecurityproject.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectForUserRole {
    private Long userId;
    private Long roleId;
}

