package com.andabazaar.serviceimpl;

import com.andabazaar.dto.eggrate.EggRateApiResponseDto;
import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.feign.EggRateApiClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EggRateApiServiceImpl Tests")
class EggRateApiServiceImplTest {
    @Mock
    private EggRateApiClient eggRateApiClient;

    @InjectMocks
    private EggRateApiServiceImpl eggRateApiService;

    private EggRateApiResponseDto apiResponse;
    private EggRateSingleResponseDto singleResponse;

    @BeforeEach
    void setUp() {
        apiResponse = EggRateApiResponseDto.builder()
                .success(true).city("Bangalore").state("Karnataka")
                .todayRate("5.50").yesterdayRate("5.00").trend("up").change("0.50").build();
        singleResponse = EggRateSingleResponseDto.builder()
                .success(true).city("Bangalore").state("Karnataka")
                .rate("5.50").trend("up").change("0.50").build();
    }

    @Nested
    @DisplayName("getEggRates")
    class GetEggRates {
        @Test
        @DisplayName("should return egg rates successfully")
        void shouldReturnEggRates() {
            when(eggRateApiClient.getEggRates("Bangalore", "Karnataka")).thenReturn(apiResponse);
            EggRateApiResponseDto result = eggRateApiService.getEggRates("Bangalore", "Karnataka");
            assertThat(result).isNotNull();
            assertThat(result.getSuccess()).isTrue();
        }

        @Test
        @DisplayName("should trim city and state")
        void shouldTrimCityAndState() {
            when(eggRateApiClient.getEggRates("Bangalore", "Karnataka")).thenReturn(apiResponse);
            EggRateApiResponseDto result = eggRateApiService.getEggRates(" Bangalore ", " Karnataka ");
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when API fails")
        void shouldThrowWhenApiFails() {
            when(eggRateApiClient.getEggRates("Bangalore", "Karnataka"))
                    .thenThrow(new RuntimeException("API error"));
            assertThatThrownBy(() -> eggRateApiService.getEggRates("Bangalore", "Karnataka"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Failed to fetch egg rates");
        }

        @Test
        @DisplayName("should throw when city is null")
        void shouldThrowWhenCityIsNull() {
            assertThatThrownBy(() -> eggRateApiService.getEggRates(null, "Karnataka"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("City is required");
        }

        @Test
        @DisplayName("should throw when city is blank")
        void shouldThrowWhenCityIsBlank() {
            assertThatThrownBy(() -> eggRateApiService.getEggRates("  ", "Karnataka"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("City is required");
        }

        @Test
        @DisplayName("should throw when state is null")
        void shouldThrowWhenStateIsNull() {
            assertThatThrownBy(() -> eggRateApiService.getEggRates("Bangalore", null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("State is required");
        }

        @Test
        @DisplayName("should throw when state is blank")
        void shouldThrowWhenStateIsBlank() {
            assertThatThrownBy(() -> eggRateApiService.getEggRates("Bangalore", "  "))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("State is required");
        }
    }

    @Nested
    @DisplayName("getTodayRate")
    class GetTodayRate {
        @Test
        @DisplayName("should return today rate")
        void shouldReturnTodayRate() {
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka")).thenReturn(singleResponse);
            EggRateSingleResponseDto result = eggRateApiService.getTodayRate("Bangalore", "Karnataka");
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when API fails")
        void shouldThrowWhenApiFails() {
            when(eggRateApiClient.getTodayRate("Bangalore", "Karnataka"))
                    .thenThrow(new RuntimeException("API error"));
            assertThatThrownBy(() -> eggRateApiService.getTodayRate("Bangalore", "Karnataka"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Failed to fetch today's egg rate");
        }

        @Test
        @DisplayName("should throw when city is null")
        void shouldThrowWhenCityIsNull() {
            assertThatThrownBy(() -> eggRateApiService.getTodayRate(null, "Karnataka"))
                    .isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("getYesterdayRate")
    class GetYesterdayRate {
        @Test
        @DisplayName("should return yesterday rate")
        void shouldReturnYesterdayRate() {
            when(eggRateApiClient.getYesterdayRate("Bangalore", "Karnataka")).thenReturn(singleResponse);
            EggRateSingleResponseDto result = eggRateApiService.getYesterdayRate("Bangalore", "Karnataka");
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when API fails")
        void shouldThrowWhenApiFails() {
            when(eggRateApiClient.getYesterdayRate("Bangalore", "Karnataka"))
                    .thenThrow(new RuntimeException("API error"));
            assertThatThrownBy(() -> eggRateApiService.getYesterdayRate("Bangalore", "Karnataka"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Failed to fetch yesterday's egg rate");
        }

        @Test
        @DisplayName("should throw when state is blank")
        void shouldThrowWhenStateIsBlank() {
            assertThatThrownBy(() -> eggRateApiService.getYesterdayRate("Bangalore", ""))
                    .isInstanceOf(BadRequestException.class);
        }
    }
}
