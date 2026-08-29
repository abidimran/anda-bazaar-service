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
public class UserDashboardDto {

    // User
    private Long userId;
    private String userName;

    // Notifications
    private long unreadNotifications;

    // Today's Price
    private BigDecimal lowestEggPrice;
    private BigDecimal highestEggPrice;
    private BigDecimal averageEggPrice;
}
