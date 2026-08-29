package com.andabazaar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.dashboard.UserDashboardDto;
import com.andabazaar.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Dashboard", description = "Dashboard data for admin and users")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get Admin Dashboard")
    @GetMapping("/admin")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard() {

 return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @Operation(summary = "Get User Dashboard")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDashboardDto> getUserDashboard(@PathVariable Long userId) {

 return ResponseEntity.ok(dashboardService.getUserDashboard(userId));
    }
}