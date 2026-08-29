package com.andabazaar.serviceimpl;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.dashboard.UserDashboardDto;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.PaymentRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.entity.EggPrice;
import com.andabazaar.repository.entity.User;
import com.andabazaar.service.DashboardService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final EggPriceRepository eggPriceRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public AdminDashboardDto getAdminDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(UserStatus.ACTIVE);
        long inactiveUsers = userRepository.countByStatus(UserStatus.INACTIVE);
        long todayPriceCount = eggPriceRepository
                .findByPriceDateOrderByPriceDateDesc(today)
                .size();
        long yesterdayPriceCount = eggPriceRepository
                .findByPriceDateOrderByPriceDateDesc(yesterday)
                .size();
        long totalPayments = paymentRepository.count();
        BigDecimal totalRevenue = calculateTotalRevenue();
        long unreadNotifications = notificationRepository
                .findAll()
                .stream()
                .filter(notification -> Boolean.FALSE.equals(notification.getRead()))
                .count();
        return AdminDashboardDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .todayPriceCount(todayPriceCount)
                .yesterdayPriceCount(yesterdayPriceCount)
                .totalPayments(totalPayments)
                .totalRevenue(totalRevenue)
                .unreadNotifications(unreadNotifications)
                .build();
    }

    @Override
    public UserDashboardDto getUserDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        LocalDate today = LocalDate.now();
        long unreadNotifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(notification -> Boolean.FALSE.equals(notification.getRead()))
                .count();
        List<EggPrice> todayPrices = eggPriceRepository
                .findByPriceDateOrderByPriceDateDesc(today);
        BigDecimal lowestEggPrice = getLowestPrice(todayPrices);
        BigDecimal highestEggPrice = getHighestPrice(todayPrices);
        BigDecimal averageEggPrice = getAveragePrice(todayPrices);
        String userName = buildUserName(user);
        return UserDashboardDto.builder()
                .userId(user.getId())
                .userName(userName)
                .unreadNotifications(unreadNotifications)
                .lowestEggPrice(lowestEggPrice)
                .highestEggPrice(highestEggPrice)
                .averageEggPrice(averageEggPrice)
                .build();
    }

    private BigDecimal calculateTotalRevenue() {
        return paymentRepository
                .findAll()
                .stream()
                .map(payment -> payment.getAmount())
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getLowestPrice(List<EggPrice> prices) {
        return prices.stream()
                .map(EggPrice::getPricePerEgg)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getHighestPrice(List<EggPrice> prices) {
        return prices.stream()
                .map(EggPrice::getPricePerEgg)
                .filter(price -> price != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getAveragePrice(List<EggPrice> prices) {
        List<BigDecimal> validPrices = prices.stream()
                .map(EggPrice::getPricePerEgg)
                .filter(price -> price != null)
                .toList();
        if (validPrices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = validPrices.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide( BigDecimal.valueOf(validPrices.size()), 2, RoundingMode.HALF_UP);
    }

    private String buildUserName(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
