package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.dashboard.UserDashboardDto;
import com.andabazaar.entity.EggPrice;
import com.andabazaar.entity.User;
import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.CouponStatus;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.enums.TicketStatus;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CouponRepository;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.FavoriteMarketRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.PaymentRepository;
import com.andabazaar.repository.PriceAlertRepository;
import com.andabazaar.repository.PriceReportRepository;
import com.andabazaar.repository.SupportTicketRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.UserSubscriptionRepository;
import com.andabazaar.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;

    private final MarketRepository marketRepository;

    private final EggPriceRepository eggPriceRepository;

    private final UserSubscriptionRepository subscriptionRepository;

    private final PaymentRepository paymentRepository;

    private final NotificationRepository notificationRepository;

    private final FavoriteMarketRepository favoriteMarketRepository;

    private final PriceAlertRepository priceAlertRepository;

    private final PriceReportRepository priceReportRepository;

    private final SupportTicketRepository supportTicketRepository;

    private final CouponRepository couponRepository;


    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @Override
    public AdminDashboardDto getAdminDashboard() {

        LocalDate today = LocalDate.now();

        LocalDate yesterday = today.minusDays(1);


        // =========================
        // USERS
        // =========================

        long totalUsers = userRepository.count();

        long activeUsers =
                userRepository.countByStatus( UserStatus.ACTIVE);

        long inactiveUsers =
                userRepository.countByStatus( UserStatus.INACTIVE);


        // =========================
        // MARKETS
        // =========================

        long totalMarkets =
                marketRepository.count();

        long activeMarkets =
                marketRepository
                        .findByActiveTrueOrderByNameAsc()
                        .size();


        // =========================
        // EGG PRICES
        // =========================

        long todayPriceCount =
                eggPriceRepository
                        .findByPriceDateOrderByPriceDateDesc( today )
                        .size();

        long yesterdayPriceCount =
                eggPriceRepository
                        .findByPriceDateOrderByPriceDateDesc( yesterday )
                        .size();


        // =========================
        // SUBSCRIPTIONS
        // =========================

        long activeSubscriptions =
                subscriptionRepository.countByStatus( SubscriptionStatus.ACTIVE);

        long expiredSubscriptions =
                subscriptionRepository.countByStatus( SubscriptionStatus.EXPIRED);


        // =========================
        // PAYMENTS
        // =========================

        long totalPayments =
                paymentRepository.count();

        BigDecimal totalRevenue =
                calculateTotalRevenue();


        // =========================
        // NOTIFICATIONS
        // =========================

        long unreadNotifications =
                notificationRepository
                        .findAll()
                        .stream()
                        .filter(notification ->
                                Boolean.FALSE.equals( notification.getRead() )
                        )
                        .count();


        // =========================
        // PRICE REPORTS
        // =========================

        long pendingPriceReports =
                priceReportRepository.countByStatus( "PENDING");


        // =========================
        // SUPPORT TICKETS
        // =========================

        long openSupportTickets =
                supportTicketRepository
                        .countByStatus( TicketStatus.OPEN);

        long closedSupportTickets =
                supportTicketRepository
                        .countByStatus( TicketStatus.CLOSED);


        // =========================
        // COUPONS
        // =========================

        long activeCoupons =
                couponRepository
                        .findByStatusOrderByCreatedAtDesc( CouponStatus.ACTIVE )
                        .size();

        long expiredCoupons =
                couponRepository
                        .findByStatusOrderByCreatedAtDesc( CouponStatus.EXPIRED )
                        .size();


        // =========================
        // RESPONSE
        // =========================

        return AdminDashboardDto.builder()

                .totalUsers(totalUsers)

                .activeUsers(activeUsers)

                .inactiveUsers(inactiveUsers)

                .totalMarkets(totalMarkets)

                .activeMarkets(activeMarkets)

                .todayPriceCount(todayPriceCount)

                .yesterdayPriceCount( yesterdayPriceCount )

                .activeSubscriptions( activeSubscriptions )

                .expiredSubscriptions( expiredSubscriptions )

                .totalPayments(totalPayments)

                .totalRevenue(totalRevenue)

                .unreadNotifications( unreadNotifications )

                .pendingPriceReports( pendingPriceReports )

                .openSupportTickets( openSupportTickets )

                .closedSupportTickets( closedSupportTickets )

                .activeCoupons(activeCoupons)

                .expiredCoupons(expiredCoupons)

                .build();
    }


    // =========================================================
    // USER DASHBOARD
    // =========================================================

    @Override
    public UserDashboardDto getUserDashboard( Long userId) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + userId
                                ));


        LocalDate today =
                LocalDate.now();


        // =========================
        // SUBSCRIPTION
        // =========================

        UserSubscription subscription =
                subscriptionRepository
                        .findFirstByUserIdAndStatusOrderByEndDateDesc( userId, SubscriptionStatus.ACTIVE )
                        .orElse(null);


        boolean hasActiveSubscription =
                false;

        String subscriptionPlanName =
                null;

        LocalDate subscriptionStartDate =
                null;

        LocalDate subscriptionEndDate =
                null;

        long subscriptionDaysRemaining =
                0;


        if (subscription != null
                && subscription.getEndDate() != null
                && !subscription.getEndDate()
                        .isBefore(today)) {

            hasActiveSubscription = true;


            if (subscription.getPlan() != null) {

                subscriptionPlanName =
                        subscription.getPlan().getName();
            }


            subscriptionStartDate =
                    subscription.getStartDate();

            subscriptionEndDate =
                    subscription.getEndDate();


            subscriptionDaysRemaining =
                    ChronoUnit.DAYS.between( today, subscriptionEndDate);
        }


        // =========================
        // FAVORITE MARKETS
        // =========================

        long favoriteMarketCount =
                favoriteMarketRepository
                        .findAll()
                        .stream()
                        .filter(favorite ->
                                favorite.getUser() != null
                                && favorite.getUser()
                                        .getId()
                                        .equals(userId)
                        )
                        .count();


        // =========================
        // PRICE ALERTS
        // =========================

        long activePriceAlerts =
                priceAlertRepository
                        .findAll()
                        .stream()
                        .filter(alert ->
                                alert.getUser() != null
                                && alert.getUser()
                                        .getId()
                                        .equals(userId)
                                && Boolean.TRUE.equals( alert.getActive() )
                        )
                        .count();


        // =========================
        // PRICE REPORTS
        // =========================

        long totalPriceReports =
                priceReportRepository
                        .countByUserId(userId);


        long pendingPriceReports =
                countPendingUserReports(userId);


        // =========================
        // NOTIFICATIONS
        // =========================

        long unreadNotifications =
                notificationRepository
                        .findByUserIdOrderByCreatedAtDesc( userId )
                        .stream()
                        .filter(notification ->
                                Boolean.FALSE.equals( notification.getRead() )
                        )
                        .count();


        // =========================
        // SUPPORT TICKETS
        // =========================

        long openSupportTickets =
                supportTicketRepository
                        .findByUserIdOrderByCreatedAtDesc( userId )
                        .stream()
                        .filter(ticket ->
                                ticket.getStatus()
                                        == TicketStatus.OPEN
                        )
                        .count();


        // =========================
        // TODAY EGG PRICES
        // =========================

        List<EggPrice> todayPrices =
                eggPriceRepository
                        .findByPriceDateOrderByPriceDateDesc( today);


        BigDecimal lowestEggPrice =
                getLowestPrice(todayPrices);


        BigDecimal highestEggPrice =
                getHighestPrice(todayPrices);


        BigDecimal averageEggPrice =
                getAveragePrice(todayPrices);


        // =========================
        // USER NAME
        // =========================

        String userName =
                buildUserName(user);


        // =========================
        // RESPONSE
        // =========================

        return UserDashboardDto.builder()

                .userId(user.getId())

                .userName(userName)

                .hasActiveSubscription( hasActiveSubscription )

                .subscriptionPlanName( subscriptionPlanName )

                .subscriptionStartDate( subscriptionStartDate )

                .subscriptionEndDate( subscriptionEndDate )

                .subscriptionDaysRemaining( subscriptionDaysRemaining )

                .favoriteMarketCount( favoriteMarketCount )

                .activePriceAlerts( activePriceAlerts )

                .totalPriceReports( totalPriceReports )

                .pendingPriceReports( pendingPriceReports )

                .unreadNotifications( unreadNotifications )

                .openSupportTickets( openSupportTickets )

                .lowestEggPrice( lowestEggPrice )

                .highestEggPrice( highestEggPrice )

                .averageEggPrice( averageEggPrice )

                .build();
    }


    // =========================================================
    // TOTAL REVENUE
    // =========================================================

    private BigDecimal calculateTotalRevenue() {

        return paymentRepository
                .findAll()
                .stream()
                .map(payment -> payment.getAmount())
                .filter(amount -> amount != null)
                .reduce( BigDecimal.ZERO, BigDecimal::add);
    }


    // =========================================================
    // PENDING PRICE REPORTS
    // =========================================================

    private long countPendingUserReports( Long userId) {

        return priceReportRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(report ->
                        "PENDING".equals( report.getStatus() )
                )
                .count();
    }


    // =========================================================
    // LOWEST PRICE
    // =========================================================

    private BigDecimal getLowestPrice( List<EggPrice> prices) {

        return prices.stream()

                .map(EggPrice::getPricePerEgg)

                .filter(price -> price != null)

                .min(BigDecimal::compareTo)

                .orElse(BigDecimal.ZERO);
    }


    // =========================================================
    // HIGHEST PRICE
    // =========================================================

    private BigDecimal getHighestPrice( List<EggPrice> prices) {

        return prices.stream()

                .map(EggPrice::getPricePerEgg)

                .filter(price -> price != null)

                .max(BigDecimal::compareTo)

                .orElse(BigDecimal.ZERO);
    }


    // =========================================================
    // AVERAGE PRICE
    // =========================================================

    private BigDecimal getAveragePrice( List<EggPrice> prices) {

        List<BigDecimal> validPrices =
                prices.stream()

                        .map(EggPrice::getPricePerEgg)

                        .filter(price -> price != null)

                        .toList();


        if (validPrices.isEmpty()) {

            return BigDecimal.ZERO;
        }


        BigDecimal total =
                validPrices.stream()

                        .reduce( BigDecimal.ZERO, BigDecimal::add);


        return total.divide(
                BigDecimal.valueOf( validPrices.size()
                ),
                2,
                RoundingMode.HALF_UP);
    }


    // =========================================================
    // USER NAME
    // =========================================================

    private String buildUserName( User user) {

        String firstName =
                user.getFirstName() != null
                        ? user.getFirstName()
                        : "";


        String lastName =
                user.getLastName() != null
                        ? user.getLastName()
                        : "";


        return (firstName + " " + lastName)
                .trim();
    }
}