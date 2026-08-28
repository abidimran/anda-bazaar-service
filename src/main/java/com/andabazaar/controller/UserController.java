package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.user.UserProfileDto;
import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // =========================================================
    // CREATE USER
    // =========================================================

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    // =========================================================
    // GET USER
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

 return ResponseEntity.ok(userService.getUserById(id));
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

 return ResponseEntity.ok(userService.getAllUsers());
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto request) {

 return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

 return ResponseEntity.noContent().build();
    }

    // =========================================================
    // USER PROFILE
    // =========================================================

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable Long id) {

 return ResponseEntity.ok(userService.getProfile(id));
    }

    // =========================================================
    // CHANGE USER STATUS
    // =========================================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> changeStatus(@PathVariable Long id, @RequestParam String status) {

 return ResponseEntity.ok(userService.changeUserStatus(id, status));
    }
}