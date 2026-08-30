package com.andabazaar.service;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.dashboard.UserDashboardDto;

public interface DashboardService {
    AdminDashboardDto getAdminDashboard();

    UserDashboardDto getUserDashboard(Long userId);
}
