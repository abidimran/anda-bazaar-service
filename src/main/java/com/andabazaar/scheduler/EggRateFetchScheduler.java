package com.andabazaar.scheduler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;
import com.andabazaar.entity.City;
import com.andabazaar.entity.DailyEggRate;
import com.andabazaar.entity.State;
import com.andabazaar.feign.EggRateApiClient;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.DailyEggRateRepository;
import com.andabazaar.repository.StateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EggRateFetchScheduler {

    private final EggRateApiClient eggRateApiClient;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final DailyEggRateRepository dailyEggRateRepository;

    @Scheduled(fixedRate = 3600000)
    public void fetchAndSaveRates() {

        log.info("EggRateFetchScheduler started");

        LocalDate today = LocalDate.now();
        List<State> states = stateRepository.findByActiveTrueOrderByNameAsc();

        if (states.isEmpty()) {
            log.warn("No active states found, skipping");
            return;
        }

        int saved = 0;
        int updated = 0;
        int skipped = 0;
        int failed = 0;

        for (State state : states) {

            List<City> cities = cityRepository.findByStateIdAndActiveTrueOrderByNameAsc(state.getId());

            for (City city : cities) {
                try {
                    EggRateSingleResponseDto response = eggRateApiClient.getTodayRate(city.getName(), state.getName());

                    if (response == null || response.getSuccess() == null || !response.getSuccess()) {
                        skipped++;
                        continue;
                    }

                    BigDecimal rate = new BigDecimal(response.getRate());
                    BigDecimal changeAmount = response.getChange() != null ? new BigDecimal(response.getChange()) : BigDecimal.ZERO;

                    BigDecimal previousRate = dailyEggRateRepository
                            .findTopByCityIdAndRateDateBeforeOrderByRateDateDesc(city.getId(), today)
                            .map(DailyEggRate::getRate)
                            .orElse(null);

                    Optional<DailyEggRate> existing = dailyEggRateRepository.findByCityIdAndRateDate(city.getId(), today);

                    if (existing.isPresent()) {
                        DailyEggRate record = existing.get();
                        record.setRate(rate);
                        record.setPreviousRate(previousRate);
                        record.setTrend(response.getTrend());
                        record.setChange(changeAmount);
                        record.setSource("RapidAPI");
                        dailyEggRateRepository.save(record);
                        updated++;
                    } else {
                        DailyEggRate record = DailyEggRate.builder()
                                .city(city)
                                .state(state)
                                .rateDate(today)
                                .rate(rate)
                                .previousRate(previousRate)
                                .trend(response.getTrend())
                                .change(changeAmount)
                                .source("RapidAPI")
                                .build();
                        dailyEggRateRepository.save(record);
                        saved++;
                    }

                } catch (Exception e) {
                    failed++;
                    log.debug("No rate for city={} state={}: {}", city.getName(), state.getName(), e.getMessage());
                }
            }
        }

        log.info("EggRateFetchScheduler completed — saved={}, updated={}, skipped={}, failed={}", saved, updated, skipped, failed);
    }
}
