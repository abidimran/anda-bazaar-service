package com.andabazaar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.auth.LoginRequestDto;
import com.andabazaar.dto.auth.LoginResponseDto;
import com.andabazaar.dto.auth.RegisterRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication", description = "User registration, login, and session")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {

 return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Get Current User")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser( Authentication authentication) {

        String email = authentication.getName();

 return ResponseEntity.ok(authService.getCurrentUser(email));
    }
}