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

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.ExpectedPrice;
import com.andabazaar.repository.entity.Market;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.ExpectedPriceMapper;
import com.andabazaar.repository.ExpectedPriceRepository;
import com.andabazaar.repository.MarketRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpectedPriceServiceImpl Tests")
class ExpectedPriceServiceImplTest {

    @Mock private ExpectedPriceRepository expectedPriceRepository;
    @Mock private MarketRepository marketRepository;
    @Mock private ExpectedPriceMapper expectedPriceMapper;

    @InjectMocks
    private ExpectedPriceServiceImpl expectedPriceService;

    private City city;
    private Market market;
    private ExpectedPrice expectedPrice;
    private ExpectedPriceRequestDto requestDto;

    @BeforeEach
    void setUp() {
        city = City.builder().id(1L).name("Bangalore").build();
        market = Market.builder().id(1L).name("Main Market").city(city).active(true).build();

        expectedPrice = ExpectedPrice.builder()
                .id(1L).market(market).expectedDate(LocalDate.now().plusDays(1))
                .expectedPrice(new BigDecimal("6.00")).reason("Demand increase")
                .active(true).build();

        requestDto = ExpectedPriceRequestDto.builder()
                .marketId(1L).expectedDate(LocalDate.now().plusDays(1))
                .expectedPrice(new BigDecimal("6.00")).reason("Demand increase").build();

        lenient().when(expectedPriceMapper.toResponseDto(any(ExpectedPrice.class))).thenAnswer(inv -> {
            ExpectedPrice ep = inv.getArgument(0);
            Market m = ep.getMarket();
            City c = m != null ? m.getCity() : null;
            return ExpectedPriceResponseDto.builder()
                    .id(ep.getId()).marketId(m != null ? m.getId() : null)
                    .marketName(m != null ? m.getName() : null)
                    .cityName(c != null ? c.getName() : null)
                    .expectedPrice(ep.getExpectedPrice()).expectedDate(ep.getExpectedDate())
                    .reason(ep.getReason()).active(ep.getActive())
                    .createdAt(ep.getCreatedAt()).updatedAt(ep.getUpdatedAt()).build();
        });
    }

    @Nested
    @DisplayName("createExpectedPrice")
    class CreateExpectedPrice {

        @Test
        @DisplayName("should create expected price successfully")
        void shouldCreateSuccessfully() {
            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(expectedPriceRepository.existsByMarketIdAndExpectedDate(1L, requestDto.getExpectedDate())).thenReturn(false);
            when(expectedPriceRepository.save(any(ExpectedPrice.class))).thenReturn(expectedPrice);

            ExpectedPriceResponseDto result = expectedPriceService.createExpectedPrice(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getMarketId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when already exists")
        void shouldThrowWhenExists() {
            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(expectedPriceRepository.existsByMarketIdAndExpectedDate(1L, requestDto.getExpectedDate())).thenReturn(true);

            assertThatThrownBy(() -> expectedPriceService.createExpectedPrice(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("already exists");
        }

        @Test
        @DisplayName("should throw when market not found")
        void shouldThrowWhenMarketNotFound() {
            when(marketRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> expectedPriceService.createExpectedPrice(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateExpectedPrice")
    class UpdateExpectedPrice {

        @Test
        @DisplayName("should update successfully when same market and date")
        void shouldUpdateSameMarketDate() {
            when(expectedPriceRepository.findById(1L)).thenReturn(Optional.of(expectedPrice));
            when(marketRepository.findById(1L)).thenReturn(Optional.of(market));
            when(expectedPriceRepository.save(any(ExpectedPrice.class))).thenReturn(expectedPrice);

            ExpectedPriceResponseDto result = expectedPriceService.updateExpectedPrice(1L, requestDto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when changing market/date and duplicate exists")
        void shouldThrowOnDuplicate() {
            requestDto.setMarketId(2L);
            Market market2 = Market.builder().id(2L).name("M2").city(city).active(true).build();

            when(expectedPriceRepository.findById(1L)).thenReturn(Optional.of(expectedPrice));
            when(marketRepository.findById(2L)).thenReturn(Optional.of(market2));
            when(expectedPriceRepository.existsByMarketIdAndExpectedDate(2L, requestDto.getExpectedDate())).thenReturn(true);

            assertThatThrownBy(() -> expectedPriceService.updateExpectedPrice(1L, requestDto))
                    .isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("getExpectedPriceById")
    class GetById {

        @Test
        @DisplayName("should return by id")
        void shouldReturnById() {
            when(expectedPriceRepository.findById(1L)).thenReturn(Optional.of(expectedPrice));

            ExpectedPriceResponseDto result = expectedPriceService.getExpectedPriceById(1L);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(expectedPriceRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> expectedPriceService.getExpectedPriceById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByMarket")
    class GetByMarket {

        @Test
        @DisplayName("should return prices by market")
        void shouldReturnByMarket() {
            when(marketRepository.existsById(1L)).thenReturn(true);
            when(expectedPriceRepository.findByMarketIdOrderByExpectedDateDesc(1L))
                    .thenReturn(List.of(expectedPrice));

            List<ExpectedPriceResponseDto> result = expectedPriceService.getByMarket(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should throw when market not found")
        void shouldThrowWhenMarketNotFound() {
            when(marketRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> expectedPriceService.getByMarket(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should filter inactive prices")
        void shouldFilterInactivePrices() {
            ExpectedPrice inactive = ExpectedPrice.builder()
                    .id(2L).market(market).expectedDate(LocalDate.now())
                    .expectedPrice(new BigDecimal("5.00")).active(false).build();

            when(marketRepository.existsById(1L)).thenReturn(true);
            when(expectedPriceRepository.findByMarketIdOrderByExpectedDateDesc(1L))
                    .thenReturn(List.of(expectedPrice, inactive));

            List<ExpectedPriceResponseDto> result = expectedPriceService.getByMarket(1L);

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getByMarketAndDate")
    class GetByMarketAndDate {

        @Test
        @DisplayName("should return by market and date")
        void shouldReturn() {
            when(expectedPriceRepository.findByMarketIdAndExpectedDate(1L, LocalDate.now()))
                    .thenReturn(Optional.of(expectedPrice));

            ExpectedPriceResponseDto result = expectedPriceService.getByMarketAndDate(1L, LocalDate.now());

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(expectedPriceRepository.findByMarketIdAndExpectedDate(1L, LocalDate.now()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> expectedPriceService.getByMarketAndDate(1L, LocalDate.now()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when found but not active")
        void shouldThrowWhenNotActive() {
            expectedPrice.setActive(false);
            when(expectedPriceRepository.findByMarketIdAndExpectedDate(1L, LocalDate.now()))
                    .thenReturn(Optional.of(expectedPrice));

            assertThatThrownBy(() -> expectedPriceService.getByMarketAndDate(1L, LocalDate.now()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("not active");
        }
    }

    @Nested
    @DisplayName("getActiveExpectedPrices")
    class GetActive {

        @Test
        @DisplayName("should return active expected prices")
        void shouldReturnActive() {
            when(expectedPriceRepository.findByActiveTrueOrderByExpectedDateDesc())
                    .thenReturn(List.of(expectedPrice));

            List<ExpectedPriceResponseDto> result = expectedPriceService.getActiveExpectedPrices();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getByDateRange")
    class GetByDateRange {

        @Test
        @DisplayName("should return by date range")
        void shouldReturn() {
            LocalDate start = LocalDate.now();
            LocalDate end = LocalDate.now().plusDays(7);

            when(expectedPriceRepository.findByExpectedDateBetweenOrderByExpectedDateDesc(start, end))
                    .thenReturn(List.of(expectedPrice));

            List<ExpectedPriceResponseDto> result = expectedPriceService.getByDateRange(start, end);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should throw when start after end")
        void shouldThrowWhenStartAfterEnd() {
            assertThatThrownBy(() -> expectedPriceService.getByDateRange(LocalDate.now().plusDays(7), LocalDate.now()))
                    .isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("should throw when date is null")
        void shouldThrowWhenNull() {
            assertThatThrownBy(() -> expectedPriceService.getByDateRange(null, LocalDate.now()))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("required");
        }
    }

    @Nested
    @DisplayName("getMarketDateRange")
    class GetMarketDateRange {

        @Test
        @DisplayName("should return market date range")
        void shouldReturn() {
            LocalDate start = LocalDate.now();
            LocalDate end = LocalDate.now().plusDays(7);

            when(marketRepository.existsById(1L)).thenReturn(true);
            when(expectedPriceRepository.findByMarketIdAndExpectedDateBetweenOrderByExpectedDateDesc(1L, start, end))
                    .thenReturn(List.of(expectedPrice));

            List<ExpectedPriceResponseDto> result = expectedPriceService.getMarketDateRange(1L, start, end);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should throw when market not found")
        void shouldThrowWhenMarketNotFound() {
            when(marketRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> expectedPriceService.getMarketDateRange(99L, LocalDate.now(), LocalDate.now().plusDays(7)))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteExpectedPrice")
    class Delete {

        @Test
        @DisplayName("should soft delete")
        void shouldSoftDelete() {
            when(expectedPriceRepository.findById(1L)).thenReturn(Optional.of(expectedPrice));
            when(expectedPriceRepository.save(any())).thenReturn(expectedPrice);

            expectedPriceService.deleteExpectedPrice(1L);

            assertThat(expectedPrice.getActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("countActiveExpectedPrices")
    class Count {

        @Test
        @DisplayName("should return count")
        void shouldReturnCount() {
            when(expectedPriceRepository.countByActiveTrue()).thenReturn(5L);

            long result = expectedPriceService.countActiveExpectedPrices();

            assertThat(result).isEqualTo(5L);
        }
    }
}
