package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

@Tag(name = "Users", description = "User account management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create User")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @Operation(summary = "Get User By Id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

 return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get All Users")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

 return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Update User")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto request) {

 return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Delete User")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

 return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Profile")
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable Long id) {

 return ResponseEntity.ok(userService.getProfile(id));
    }

    @Operation(summary = "Change Status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> changeStatus(@PathVariable Long id, @RequestParam String status) {

 return ResponseEntity.ok(userService.changeUserStatus(id, status));
    }
}