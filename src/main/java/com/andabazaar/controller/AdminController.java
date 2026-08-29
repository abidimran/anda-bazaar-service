package com.andabazaar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.service.AdminService;
import com.andabazaar.service.DashboardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin", description = "Admin dashboard and user management")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;
    private final AdminService adminService;

    @Operation(summary = "Get Admin Dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto>
            getAdminDashboard() {

 return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @Operation(summary = "Create Admin")
    @PostMapping()
    public ResponseEntity<UserResponseDto> createAdmin(@Valid @RequestBody UserRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(request));
    }

    @Operation(summary = "Get All Users")
    @GetMapping("/users")
    public ResponseEntity<PagedResponse<UserResponseDto>>
            getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(adminService.getAllUsers(), page, size));
    }

    @Operation(summary = "Get User")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto>
            getUser(@PathVariable Long id) {

 return ResponseEntity.ok(adminService.getUser(id));
    }

    @Operation(summary = "Change User Status")
    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserResponseDto>
            changeUserStatus(@PathVariable Long id, @RequestParam String status) {

 return ResponseEntity.ok(adminService.changeUserStatus(id, status));
    }

    @Operation(summary = "Delete User")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void>
            deleteUser(@PathVariable Long id) {

        adminService.deleteUser(id);

 return ResponseEntity.noContent().build();
    }
}
