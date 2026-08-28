package com.andabazaar.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.andabazaar.repository.EggPriceRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceUpdateScheduler {

    private final EggPriceRepository eggPriceRepository;

    // DAILY PRICE CHECK

    @Scheduled(cron = "0 0 6 * * *")
    public void checkTodayPrices() {

        LocalDate today = LocalDate.now();

        long todayPriceCount =
                eggPriceRepository
                        .findByPriceDateOrderByPriceDateDesc(today)
                        .size();

        if (todayPriceCount == 0) {

            System.out.println("WARNING: No egg prices found for today: " + today);

        } else {

            System.out.println("Egg prices available for " + today + ". Total prices: " + todayPriceCount);
        }
    }
}