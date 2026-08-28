package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.service.AdminService;
import com.andabazaar.service.DashboardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;
    private final AdminService adminService;

    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto>
            getAdminDashboard() {

 return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    // =========================================================
    // CREATE ADMIN
    // =========================================================

    @PostMapping("/create-admin")
    public ResponseEntity<UserResponseDto> createAdmin(@Valid @RequestBody UserRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(request));
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>>
            getAllUsers() {

 return ResponseEntity.ok(adminService.getAllUsers());
    }

    // =========================================================
    // GET USER
    // =========================================================

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto>
            getUser(@PathVariable Long id) {

 return ResponseEntity.ok(adminService.getUser(id));
    }

    // =========================================================
    // CHANGE USER STATUS
    // =========================================================

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserResponseDto>
            changeUserStatus(@PathVariable Long id, @RequestParam String status) {

 return ResponseEntity.ok(adminService.changeUserStatus(id, status));
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void>
            deleteUser(@PathVariable Long id) {

        adminService.deleteUser(id);

 return ResponseEntity.noContent().build();
    }
}