package com.andabazaar.serviceimpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.EggPrice;
import com.andabazaar.repository.entity.Market;
import com.andabazaar.repository.entity.State;
import com.andabazaar.repository.entity.UserSubscription;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.UserSubscriptionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("EggPriceServiceImpl Tests")
class EggPriceServiceImplTest {

    @Mock
    private EggPriceRepository eggPriceRepository;

    @Mock
    private MarketRepository marketRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @InjectMocks
    private EggPriceServiceImpl eggPriceService;

    private State state;
    private City city;
    private Market market;
    private EggPrice eggPrice;
    private EggPriceRequestDto requestDto;

    @BeforeEach
    void setUp() {
        state = State.builder().id(1L).name("Karnataka").active(true).build();
        city = City.builder().id(1L).name("Bangalore").state(state).active(true).build();
        market = Market.builder().id(1L).name("Main Market").city(city).active(true).build();

        eggPrice = EggPrice.builder()
                .id(1L)
                .market(market)
                .priceDate(LocalDate.now())
                .pricePerEgg(new BigDecimal("5.50"))
                .pricePerTray(new BigDecimal("165.00"))
                .previousPrice(new BigDecimal("5.00"))
                .priceChangeType("INCREASE")
                .priceChangeAmount(new BigDecimal("0.50"))
                .remarks("Test")
                .active(true)
                .build();

        requestDto = EggPriceRequestDto.builder()
                .marketId(1L)
                .priceDate(LocalDate.now())
                .pricePerEgg(new BigDecimal("5.50"))
                .pricePerTray(new BigDecimal("165.00"))
                .remarks("Test")
                .build();
    }

    @Nested
    @DisplayName("createPrice")
    class CreatePrice {

        @Test
        @DisplayName("should create price successfully")
        void shouldCreatePriceSuccessfully() {
            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(eggPriceRepository.existsByMarketIdAndPriceDate(1L, requestDto.getPriceDate())).thenReturn(false);
            when(eggPriceRepository.findByMarketIdOrderByPriceDateDesc(1L)).thenReturn(Collections.emptyList());
            when(eggPriceRepository.save(any(EggPrice.class))).thenReturn(eggPrice);

            EggPriceResponseDto result = eggPriceService.createPrice(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getMarketId()).isEqualTo(1L);
            assertThat(result.getPricePerEgg()).isEqualByComparingTo(new BigDecimal("5.50"));
            verify(eggPriceRepository).save(any(EggPrice.class));
        }

        @Test
        @DisplayName("should create price with previous price existing")
        void shouldCreatePriceWithPreviousPrice() {
            EggPrice previousEggPrice = EggPrice.builder()
                    .id(2L).market(market).priceDate(LocalDate.now().minusDays(1))
                    .pricePerEgg(new BigDecimal("5.00")).pricePerTray(new BigDecimal("150.00"))
                    .active(true).build();

            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(eggPriceRepository.existsByMarketIdAndPriceDate(1L, requestDto.getPriceDate())).thenReturn(false);
            when(eggPriceRepository.findByMarketIdOrderByPriceDateDesc(1L)).thenReturn(List.of(previousEggPrice));
            when(eggPriceRepository.save(any(EggPrice.class))).thenReturn(eggPrice);

            EggPriceResponseDto result = eggPriceService.createPrice(requestDto);

            assertThat(result).isNotNull();
            verify(eggPriceRepository).save(any(EggPrice.class));
        }

        @Test
        @DisplayName("should throw when price already exists for market and date")
        void shouldThrowWhenPriceAlreadyExists() {
            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(eggPriceRepository.existsByMarketIdAndPriceDate(1L, requestDto.getPriceDate())).thenReturn(true);

            assertThatThrownBy(() -> eggPriceService.createPrice(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Price already exists for this market and date");
        }

        @Test
        @DisplayName("should throw when market not found")
        void shouldThrowWhenMarketNotFound() {
            when(marketRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eggPriceService.createPrice(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Market not found");
        }
    }

    @Nested
    @DisplayName("updatePrice")
    class UpdatePrice {

        @Test
        @DisplayName("should update price successfully")
        void shouldUpdatePriceSuccessfully() {
            when(eggPriceRepository.findById(1L)).thenReturn(Optional.of(eggPrice));
            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(eggPriceRepository.findByMarketIdOrderByPriceDateDesc(1L)).thenReturn(Collections.emptyList());
            when(eggPriceRepository.save(any(EggPrice.class))).thenReturn(eggPrice);

            EggPriceResponseDto result = eggPriceService.updatePrice(1L, requestDto);

            assertThat(result).isNotNull();
            verify(eggPriceRepository).save(any(EggPrice.class));
        }

        @Test
        @DisplayName("should throw when changing market/date and duplicate exists")
        void shouldThrowWhenDuplicateOnUpdate() {
            EggPriceRequestDto differentMarketReq = EggPriceRequestDto.builder()
                    .marketId(2L)
                    .priceDate(LocalDate.now())
                    .pricePerEgg(new BigDecimal("5.50"))
                    .pricePerTray(new BigDecimal("165.00"))
                    .build();

            Market market2 = Market.builder().id(2L).name("Market 2").city(city).active(true).build();

            when(eggPriceRepository.findById(1L)).thenReturn(Optional.of(eggPrice));
            when(marketRepository.findById(2L)).thenReturn(Optional.of(market2));
            when(eggPriceRepository.existsByMarketIdAndPriceDate(2L, LocalDate.now())).thenReturn(true);

            assertThatThrownBy(() -> eggPriceService.updatePrice(1L, differentMarketReq))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Price already exists for this market and date");
        }

        @Test
        @DisplayName("should throw when price not found")
        void shouldThrowWhenPriceNotFound() {
            when(eggPriceRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eggPriceService.updatePrice(99L, requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Egg price not found");
        }
    }

    @Nested
    @DisplayName("getPriceById")
    class GetPriceById {

        @Test
        @DisplayName("should return price by id")
        void shouldReturnPriceById() {
            when(eggPriceRepository.findById(1L)).thenReturn(Optional.of(eggPrice));

            EggPriceResponseDto result = eggPriceService.getPriceById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when price not found")
        void shouldThrowWhenNotFound() {
            when(eggPriceRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eggPriceService.getPriceById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getMarketPrice")
    class GetMarketPrice {

        @Test
        @DisplayName("should return market price")
        void shouldReturnMarketPrice() {
            when(eggPriceRepository.findByMarketIdAndPriceDate(1L, LocalDate.now()))
                    .thenReturn(Optional.of(eggPrice));

            EggPriceResponseDto result = eggPriceService.getMarketPrice(1L, LocalDate.now());

            assertThat(result).isNotNull();
            assertThat(result.getMarketId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(eggPriceRepository.findByMarketIdAndPriceDate(1L, LocalDate.now()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> eggPriceService.getMarketPrice(1L, LocalDate.now()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getTodayPrices")
    class GetTodayPrices {

        @Test
        @DisplayName("should return today prices")
        void shouldReturnTodayPrices() {
            when(eggPriceRepository.findAll()).thenReturn(List.of(eggPrice));

            List<EggPriceResponseDto> result = eggPriceService.getTodayPrices();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should return empty when no prices for today")
        void shouldReturnEmptyWhenNoPricesForToday() {
            EggPrice yesterday = EggPrice.builder()
                    .id(2L).market(market).priceDate(LocalDate.now().minusDays(1))
                    .pricePerEgg(new BigDecimal("5.00")).pricePerTray(new BigDecimal("150.00"))
                    .active(true).build();

            when(eggPriceRepository.findAll()).thenReturn(List.of(yesterday));

            List<EggPriceResponseDto> result = eggPriceService.getTodayPrices();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getYesterdayPrices")
    class GetYesterdayPrices {

        @Test
        @DisplayName("should return yesterday prices")
        void shouldReturnYesterdayPrices() {
            EggPrice yesterday = EggPrice.builder()
                    .id(2L).market(market).priceDate(LocalDate.now().minusDays(1))
                    .pricePerEgg(new BigDecimal("5.00")).pricePerTray(new BigDecimal("150.00"))
                    .active(true).build();

            when(eggPriceRepository.findAll()).thenReturn(List.of(yesterday));

            List<EggPriceResponseDto> result = eggPriceService.getYesterdayPrices();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getPriceHistory")
    class GetPriceHistory {

        @Test
        @DisplayName("should return price history")
        void shouldReturnPriceHistory() {
            LocalDate start = LocalDate.now().minusDays(7);
            LocalDate end = LocalDate.now();

            when(eggPriceRepository.findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(1L, start, end))
                    .thenReturn(List.of(eggPrice));

            List<EggPriceResponseDto> result = eggPriceService.getPriceHistory(1L, start, end);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should throw when start date is after end date")
        void shouldThrowWhenStartAfterEnd() {
            LocalDate start = LocalDate.now();
            LocalDate end = LocalDate.now().minusDays(7);

            assertThatThrownBy(() -> eggPriceService.getPriceHistory(1L, start, end))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Start date cannot be after end date");
        }
    }

    @Nested
    @DisplayName("getUserPrices")
    class GetUserPrices {

        @Test
        @DisplayName("should return all prices for subscribed user")
        void shouldReturnAllPricesForSubscribedUser() {
            UserSubscription subscription = UserSubscription.builder()
                    .id(1L).status(SubscriptionStatus.ACTIVE).endDate(LocalDate.now().plusDays(30)).build();

            when(userSubscriptionRepository.findFirstByUserIdAndStatusOrderByEndDateDesc(1L, SubscriptionStatus.ACTIVE))
                    .thenReturn(Optional.of(subscription));
            when(eggPriceRepository.findAll()).thenReturn(List.of(eggPrice));

            List<EggPriceResponseDto> result = eggPriceService.getUserPrices(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should filter recent prices for non-subscribed user")
        void shouldFilterRecentPricesForNonSubscribedUser() {
            when(userSubscriptionRepository.findFirstByUserIdAndStatusOrderByEndDateDesc(1L, SubscriptionStatus.ACTIVE))
                    .thenReturn(Optional.empty());

            EggPrice oldPrice = EggPrice.builder()
                    .id(3L).market(market).priceDate(LocalDate.now().minusDays(5))
                    .pricePerEgg(new BigDecimal("4.50")).pricePerTray(new BigDecimal("135.00"))
                    .active(true).build();

            when(eggPriceRepository.findAll()).thenReturn(List.of(eggPrice, oldPrice));

            List<EggPriceResponseDto> result = eggPriceService.getUserPrices(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPricePerEgg()).isEqualByComparingTo(new BigDecimal("4.50"));
        }
    }

    @Nested
    @DisplayName("getUserPriceHistory")
    class GetUserPriceHistory {

        @Test
        @DisplayName("should return history for subscribed user")
        void shouldReturnHistoryForSubscribedUser() {
            UserSubscription subscription = UserSubscription.builder()
                    .id(1L).status(SubscriptionStatus.ACTIVE).endDate(LocalDate.now().plusDays(30)).build();

            LocalDate start = LocalDate.now().minusDays(7);
            LocalDate end = LocalDate.now();

            when(userSubscriptionRepository.findFirstByUserIdAndStatusOrderByEndDateDesc(1L, SubscriptionStatus.ACTIVE))
                    .thenReturn(Optional.of(subscription));
            when(eggPriceRepository.findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(1L, start, end))
                    .thenReturn(List.of(eggPrice));

            List<EggPriceResponseDto> result = eggPriceService.getUserPriceHistory(1L, 1L, start, end);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should throw when start after end")
        void shouldThrowWhenStartAfterEnd() {
            LocalDate start = LocalDate.now();
            LocalDate end = LocalDate.now().minusDays(7);

            assertThatThrownBy(() -> eggPriceService.getUserPriceHistory(1L, 1L, start, end))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Start date cannot be after end date");
        }

        @Test
        @DisplayName("should throw when non-subscribed user requests recent prices")
        void shouldThrowWhenNonSubscribedUserRequestsRecentPrices() {
            when(userSubscriptionRepository.findFirstByUserIdAndStatusOrderByEndDateDesc(1L, SubscriptionStatus.ACTIVE))
                    .thenReturn(Optional.empty());

            LocalDate start = LocalDate.now().minusDays(1);
            LocalDate end = LocalDate.now();

            assertThatThrownBy(() -> eggPriceService.getUserPriceHistory(1L, 1L, start, end))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Active subscription is required");
        }

        @Test
        @DisplayName("should throw when non-subscribed user requests recent end date")
        void shouldThrowWhenNonSubscribedUserRequestsRecentEndDate() {
            when(userSubscriptionRepository.findFirstByUserIdAndStatusOrderByEndDateDesc(1L, SubscriptionStatus.ACTIVE))
                    .thenReturn(Optional.empty());

            // Both start and end are within the 2-day restriction window
            LocalDate start = LocalDate.now().minusDays(1);
            LocalDate end = LocalDate.now();

            assertThatThrownBy(() -> eggPriceService.getUserPriceHistory(1L, 1L, start, end))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Active subscription is required");
        }

        @Test
        @DisplayName("should clamp end date for non-subscribed user with valid older range")
        void shouldClampEndDateForNonSubscribedUser() {
            when(userSubscriptionRepository.findFirstByUserIdAndStatusOrderByEndDateDesc(1L, SubscriptionStatus.ACTIVE))
                    .thenReturn(Optional.empty());

            LocalDate start = LocalDate.now().minusDays(10);
            LocalDate end = LocalDate.now().minusDays(3);

            when(eggPriceRepository.findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(
                    eq(1L), eq(start), any(LocalDate.class)))
                    .thenReturn(List.of(eggPrice));

            List<EggPriceResponseDto> result = eggPriceService.getUserPriceHistory(1L, 1L, start, end);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("deletePrice")
    class DeletePrice {

        @Test
        @DisplayName("should soft delete price")
        void shouldSoftDeletePrice() {
            when(eggPriceRepository.findById(1L)).thenReturn(Optional.of(eggPrice));
            when(eggPriceRepository.save(any(EggPrice.class))).thenReturn(eggPrice);

            eggPriceService.deletePrice(1L);

            assertThat(eggPrice.getActive()).isFalse();
            verify(eggPriceRepository).save(eggPrice);
        }

        @Test
        @DisplayName("should throw when price not found for delete")
        void shouldThrowWhenPriceNotFoundForDelete() {
            when(eggPriceRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eggPriceService.deletePrice(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
