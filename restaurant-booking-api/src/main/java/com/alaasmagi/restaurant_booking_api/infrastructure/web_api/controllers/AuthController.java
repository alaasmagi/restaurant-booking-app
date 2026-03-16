package com.alaasmagi.restaurant_booking_api.infrastructure.web_api.controllers;

import com.alaasmagi.restaurant_booking_api.application.dtos.VerifyPasswordDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Value("${spring.security.user.name}")
    private String adminUsername;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyPassword(@Valid @RequestBody VerifyPasswordDto request) {

        if (request.getUserName() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        if(adminUsername.equals(request.getUserName()) && adminPassword.equals(request.getPassword())) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
