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

    // Markets
    private long totalMarkets;
    private long activeMarkets;

    // Egg Prices
    private long todayPriceCount;
    private long yesterdayPriceCount;

    // Subscriptions
    private long activeSubscriptions;
    private long expiredSubscriptions;

    // Payments
    private long totalPayments;
    private BigDecimal totalRevenue;

    // Notifications
    private long unreadNotifications;

    // Price Reports
    private long pendingPriceReports;

    // Support Tickets
    private long openSupportTickets;
    private long closedSupportTickets;

    // Coupons
    private long activeCoupons;
    private long expiredCoupons;
}