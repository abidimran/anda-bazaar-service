package com.andabazaar.scheduler;

import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;
import com.andabazaar.feign.EggRateApiClient;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.DailyEggRateRepository;
import com.andabazaar.repository.StateRepository;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.DailyEggRate;
import com.andabazaar.repository.entity.State;

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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EggRateFetchScheduler Tests")
class EggRateFetchSchedulerTest {
    @Mock private EggRateApiClient eggRateApiClient;

    @Mock private StateRepository stateRepository;

    @Mock private CityRepository cityRepository;

    @Mock private DailyEggRateRepository dailyEggRateRepository;

    @InjectMocks
    private EggRateFetchScheduler scheduler;

    private State state;
    private City city;

    @BeforeEach
    void setUp() {
        state = State.builder().id(1L).name("Karnataka").build();
        city = City.builder().id(1L).name("Bangalore").build();
    }

    @Nested
    @DisplayName("fetchAndSaveRates")
    class FetchAndSaveRates {
        @Test
        @DisplayName("should skip when no states found")
        void shouldSkipWhenNoStates() {
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(Collections.emptyList());
            scheduler.fetchAndSaveRates();
            verify(eggRateApiClient, never()).getTodayRate(anyString(), anyString());
        }

        @Test
        @DisplayName("should save new rate when no existing record")
        void shouldSaveNewRate() {
            EggRateSingleResponseDto response = EggRateSingleResponseDto.builder()
                    .success(true).rate("5.50").trend("up").change("0.50").build();
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(response);
            when(dailyEggRateRepository.findTopByCityIdAndRateDateBeforeOrderByRateDateDesc(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.empty());
            when(dailyEggRateRepository.findByCityIdAndRateDate(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.empty());
            when(dailyEggRateRepository.save(any(DailyEggRate.class))).thenReturn(null);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository).save(any(DailyEggRate.class));
        }

        @Test
        @DisplayName("should update existing rate")
        void shouldUpdateExistingRate() {
            EggRateSingleResponseDto response = EggRateSingleResponseDto.builder()
                    .success(true).rate("5.50").trend("up").change("0.50").build();
            DailyEggRate existing = DailyEggRate.builder()
                    .id(1L).city(city).state(state).rateDate(LocalDate.now())
                    .rate(new BigDecimal("5.00")).build();
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(response);
            when(dailyEggRateRepository.findTopByCityIdAndRateDateBeforeOrderByRateDateDesc(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.empty());
            when(dailyEggRateRepository.findByCityIdAndRateDate(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.of(existing));
            when(dailyEggRateRepository.save(any(DailyEggRate.class))).thenReturn(existing);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository).save(existing);
        }

        @Test
        @DisplayName("should skip when response is null")
        void shouldSkipWhenResponseIsNull() {
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(null);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository, never()).save(any());
        }

        @Test
        @DisplayName("should skip when response success is false")
        void shouldSkipWhenSuccessIsFalse() {
            EggRateSingleResponseDto response = EggRateSingleResponseDto.builder()
                    .success(false).build();
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(response);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository, never()).save(any());
        }

        @Test
        @DisplayName("should skip when response success is null")
        void shouldSkipWhenSuccessIsNull() {
            EggRateSingleResponseDto response = EggRateSingleResponseDto.builder()
                    .success(null).build();
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(response);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository, never()).save(any());
        }

        @Test
        @DisplayName("should handle exception gracefully and continue")
        void shouldHandleException() {
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka"))
                    .thenThrow(new RuntimeException("API error"));
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository, never()).save(any());
        }

        @Test
        @DisplayName("should handle null change in response")
        void shouldHandleNullChange() {
            EggRateSingleResponseDto response = EggRateSingleResponseDto.builder()
                    .success(true).rate("5.50").trend("stable").change(null).build();
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(response);
            when(dailyEggRateRepository.findTopByCityIdAndRateDateBeforeOrderByRateDateDesc(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.empty());
            when(dailyEggRateRepository.findByCityIdAndRateDate(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.empty());
            when(dailyEggRateRepository.save(any(DailyEggRate.class))).thenReturn(null);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository).save(argThat(rate -> rate.getChange().compareTo(BigDecimal.ZERO) == 0));
        }

        @Test
        @DisplayName("should use previous rate when available")
        void shouldUsePreviousRate() {
            EggRateSingleResponseDto response = EggRateSingleResponseDto.builder()
                    .success(true).rate("5.50").trend("up").change("0.50").build();
            DailyEggRate previousRate = DailyEggRate.builder()
                    .id(2L).rate(new BigDecimal("5.00")).build();
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(List.of(city));
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(response);
            when(dailyEggRateRepository.findTopByCityIdAndRateDateBeforeOrderByRateDateDesc(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.of(previousRate));
            when(dailyEggRateRepository.findByCityIdAndRateDate(eq(1L), any(LocalDate.class)))
                    .thenReturn(Optional.empty());
            when(dailyEggRateRepository.save(any(DailyEggRate.class))).thenReturn(null);
            scheduler.fetchAndSaveRates();
            verify(dailyEggRateRepository).save(argThat(rate ->
                    rate.getPreviousRate() != null &&
                    rate.getPreviousRate().compareTo(new BigDecimal("5.00")) == 0));
        }

        @Test
        @DisplayName("should skip when no cities found")
        void shouldSkipWhenNoCities() {
            when(stateRepository.findAllByOrderByNameAsc()).thenReturn(List.of(state));
            when(cityRepository.findAll()).thenReturn(Collections.emptyList());
            scheduler.fetchAndSaveRates();
            verify(eggRateApiClient, never()).getTodayRate(anyString(), anyString());
        }
    }
}
