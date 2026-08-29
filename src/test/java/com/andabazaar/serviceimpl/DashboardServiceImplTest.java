package com.andabazaar.serviceimpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andabazaar.dto.dashboard.AdminDashboardDto;
import com.andabazaar.dto.dashboard.UserDashboardDto;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.EggPrice;
import com.andabazaar.repository.entity.Market;
import com.andabazaar.repository.entity.Notification;
import com.andabazaar.repository.entity.Payment;
import com.andabazaar.repository.entity.State;
import com.andabazaar.repository.entity.User;
import com.andabazaar.enums.NotificationType;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.PaymentRepository;
import com.andabazaar.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardServiceImpl Tests")
class DashboardServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private EggPriceRepository eggPriceRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private NotificationRepository notificationRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private User user;
    private State state;
    private City city;
    private Market market;
    private EggPrice eggPrice;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).firstName("John").lastName("Doe")
                .email("john@test.com").phone("1234567890")
                .password("enc").role(RoleType.USER).status(UserStatus.ACTIVE).build();

        state = State.builder().id(1L).name("Karnataka").active(true).build();
        city = City.builder().id(1L).name("Bangalore").state(state).active(true).build();
        market = Market.builder().id(1L).name("Market").city(city).active(true).build();

        eggPrice = EggPrice.builder()
                .id(1L).market(market).priceDate(LocalDate.now())
                .pricePerEgg(new BigDecimal("5.50")).pricePerTray(new BigDecimal("165.00"))
                .active(true).build();
    }

    @Nested
    @DisplayName("getAdminDashboard")
    class GetAdminDashboard {

        @Test
        @DisplayName("should return admin dashboard with all metrics")
        void shouldReturnAdminDashboard() {
            when(userRepository.count()).thenReturn(100L);
            when(userRepository.countByStatus(UserStatus.ACTIVE)).thenReturn(80L);
            when(userRepository.countByStatus(UserStatus.INACTIVE)).thenReturn(20L);
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(List.of(eggPrice));
            when(paymentRepository.count()).thenReturn(200L);

            Payment payment = Payment.builder().id(1L).amount(new BigDecimal("199.00")).build();
            when(paymentRepository.findAll()).thenReturn(List.of(payment));

            Notification unreadNotif = Notification.builder()
                    .id(1L).user(user).type(NotificationType.GENERAL)
                    .title("t").message("m").read(false).sent(false).build();
            when(notificationRepository.findAll()).thenReturn(List.of(unreadNotif));

            AdminDashboardDto result = dashboardService.getAdminDashboard();

            assertThat(result.getTotalUsers()).isEqualTo(100L);
            assertThat(result.getActiveUsers()).isEqualTo(80L);
            assertThat(result.getInactiveUsers()).isEqualTo(20L);
            assertThat(result.getTodayPriceCount()).isEqualTo(1L);
            assertThat(result.getTotalPayments()).isEqualTo(200L);
            assertThat(result.getTotalRevenue()).isEqualByComparingTo(new BigDecimal("199.00"));
            assertThat(result.getUnreadNotifications()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should handle empty data gracefully")
        void shouldHandleEmptyData() {
            when(userRepository.count()).thenReturn(0L);
            when(userRepository.countByStatus(any())).thenReturn(0L);
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());
            when(paymentRepository.count()).thenReturn(0L);
            when(paymentRepository.findAll()).thenReturn(Collections.emptyList());
            when(notificationRepository.findAll()).thenReturn(Collections.emptyList());

            AdminDashboardDto result = dashboardService.getAdminDashboard();

            assertThat(result.getTotalUsers()).isZero();
            assertThat(result.getTotalRevenue()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("should handle payment with null amount")
        void shouldHandlePaymentWithNullAmount() {
            when(userRepository.count()).thenReturn(0L);
            when(userRepository.countByStatus(any())).thenReturn(0L);
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());
            when(paymentRepository.count()).thenReturn(1L);

            Payment payment = Payment.builder().id(1L).amount(null).build();
            when(paymentRepository.findAll()).thenReturn(List.of(payment));
            when(notificationRepository.findAll()).thenReturn(Collections.emptyList());

            AdminDashboardDto result = dashboardService.getAdminDashboard();

            assertThat(result.getTotalRevenue()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("getUserDashboard")
    class GetUserDashboard {

        @Test
        @DisplayName("should return user dashboard with prices")
        void shouldReturnDashboardWithPrices() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                    .thenReturn(Collections.emptyList());
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(List.of(eggPrice));

            UserDashboardDto result = dashboardService.getUserDashboard(1L);

            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(1L);
            assertThat(result.getUserName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("should return user dashboard with no prices")
        void shouldReturnDashboardWithNoPrices() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                    .thenReturn(Collections.emptyList());
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            UserDashboardDto result = dashboardService.getUserDashboard(1L);

            assertThat(result).isNotNull();
            assertThat(result.getLowestEggPrice()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(result.getHighestEggPrice()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(result.getAverageEggPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> dashboardService.getUserDashboard(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should calculate average with multiple prices")
        void shouldCalculateAverageWithMultiplePrices() {
            EggPrice price2 = EggPrice.builder()
                    .id(2L).market(market).priceDate(LocalDate.now())
                    .pricePerEgg(new BigDecimal("4.50")).pricePerTray(new BigDecimal("135.00"))
                    .active(true).build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                    .thenReturn(Collections.emptyList());
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(List.of(eggPrice, price2));

            UserDashboardDto result = dashboardService.getUserDashboard(1L);

            assertThat(result.getLowestEggPrice()).isEqualByComparingTo(new BigDecimal("4.50"));
            assertThat(result.getHighestEggPrice()).isEqualByComparingTo(new BigDecimal("5.50"));
            assertThat(result.getAverageEggPrice()).isEqualByComparingTo(new BigDecimal("5.00"));
        }

        @Test
        @DisplayName("should build username correctly with both names")
        void shouldBuildUsernameCorrectly() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                    .thenReturn(Collections.emptyList());
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            UserDashboardDto result = dashboardService.getUserDashboard(1L);

            assertThat(result.getUserName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("should handle null first/last name")
        void shouldHandleNullNames() {
            User noNameUser = User.builder()
                    .id(2L).firstName(null).lastName(null)
                    .email("noname@test.com").phone("0000000000")
                    .password("enc").role(RoleType.USER).status(UserStatus.ACTIVE).build();

            when(userRepository.findById(2L)).thenReturn(Optional.of(noNameUser));
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(2L))
                    .thenReturn(Collections.emptyList());
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            UserDashboardDto result = dashboardService.getUserDashboard(2L);

            assertThat(result.getUserName()).isEmpty();
        }

        @Test
        @DisplayName("should count unread notifications")
        void shouldCountUnreadNotifications() {
            Notification unread = Notification.builder()
                    .id(1L).user(user).type(NotificationType.GENERAL)
                    .title("t").message("m").read(false).sent(false).build();
            Notification readNotif = Notification.builder()
                    .id(2L).user(user).type(NotificationType.GENERAL)
                    .title("t2").message("m2").read(true).sent(false).build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                    .thenReturn(List.of(unread, readNotif));
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            UserDashboardDto result = dashboardService.getUserDashboard(1L);

            assertThat(result.getUnreadNotifications()).isEqualTo(1L);
        }
    }
}
