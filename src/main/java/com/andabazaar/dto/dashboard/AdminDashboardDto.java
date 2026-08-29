package com.andabazaar.dto.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDto {
    // Users
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    // Egg Prices
    private long todayPriceCount;
    private long yesterdayPriceCount;
    // Payments
    private long totalPayments;
    private BigDecimal totalRevenue;
    // Notifications
    private long unreadNotifications;
}
