package com.andabazaar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.dashboard.UserDashboardDto;
import com.andabazaar.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // =========================
    // ADMIN DASHBOARD
    // =========================

    @GetMapping("/admin")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard() {

 return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    // =========================
    // USER DASHBOARD
    // =========================

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDashboardDto> getUserDashboard(@PathVariable Long userId) {

 return ResponseEntity.ok(dashboardService.getUserDashboard(userId));
    }
}