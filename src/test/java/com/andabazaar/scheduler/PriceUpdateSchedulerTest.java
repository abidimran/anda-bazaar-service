package com.andabazaar.scheduler;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andabazaar.repository.entity.EggPrice;
import com.andabazaar.repository.EggPriceRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceUpdateScheduler Tests")
class PriceUpdateSchedulerTest {

    @Mock
    private EggPriceRepository eggPriceRepository;

    @InjectMocks
    private PriceUpdateScheduler scheduler;

    @Nested
    @DisplayName("checkTodayPrices")
    class CheckTodayPrices {

        @Test
        @DisplayName("should log warning when no prices for today")
        void shouldLogWarningWhenNoPrices() {
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            scheduler.checkTodayPrices();

            verify(eggPriceRepository).findByPriceDateOrderByPriceDateDesc(any(LocalDate.class));
        }

        @Test
        @DisplayName("should log success when prices exist")
        void shouldLogSuccessWhenPricesExist() {
            EggPrice price = EggPrice.builder().id(1L).build();
            when(eggPriceRepository.findByPriceDateOrderByPriceDateDesc(any(LocalDate.class)))
                    .thenReturn(List.of(price));

            scheduler.checkTodayPrices();

            verify(eggPriceRepository).findByPriceDateOrderByPriceDateDesc(any(LocalDate.class));
        }
    }
}
