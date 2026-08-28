package com.andabazaar.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class UserDashboardDto {

    // User
    private Long userId;
    private String userName;

    // Subscription
    private boolean hasActiveSubscription;
    private String subscriptionPlanName;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private long subscriptionDaysRemaining;

    // Favorite Markets
    private long favoriteMarketCount;

    // Price Alerts
    private long activePriceAlerts;

    // Price Reports
    private long totalPriceReports;
    private long pendingPriceReports;

    // Notifications
    private long unreadNotifications;

    // Support
    private long openSupportTickets;

    // Today's Price
    private BigDecimal lowestEggPrice;
    private BigDecimal highestEggPrice;
    private BigDecimal averageEggPrice;
}