package com.andabazaar.serviceimpl;

import org.springframework.stereotype.Service;

import com.andabazaar.dto.eggrate.EggRateApiResponseDto;
import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.feign.EggRateApiClient;
import com.andabazaar.service.EggRateApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EggRateApiServiceImpl implements EggRateApiService {

    private final EggRateApiClient eggRateApiClient;

    // =========================================================
    // GET ALL EGG RATES FROM RAPIDAPI
    // =========================================================

    @Override
    public EggRateApiResponseDto getEggRates( String city, String state) {

        validateCityAndState(city, state);

        try {

            log.info( "Fetching all egg rates for city={}, state={}", city, state);

            EggRateApiResponseDto response =
                    eggRateApiClient.getEggRates( city.trim(), state.trim());

            log.info( "All egg rates fetched successfully for city={}", city);

            return response;

        } catch (Exception e) {

            log.error( "Failed to fetch egg rates for city={}, state={}: {}", city, state, e.getMessage());

            throw new BadRequestException(
                    "Failed to fetch egg rates: "
                            + e.getMessage());
        }
    }

    // =========================================================
    // GET TODAY'S EGG RATE FROM RAPIDAPI
    // =========================================================

    @Override
    public EggRateSingleResponseDto getTodayRate( String city, String state) {

        validateCityAndState(city, state);

        try {

            log.info( "Fetching today's egg rate for city={}, state={}", city, state);

            EggRateSingleResponseDto response =
                    eggRateApiClient.getTodayRate( city.trim(), state.trim());

            log.info( "Today's egg rate fetched successfully for city={}", city);

            return response;

        } catch (Exception e) {

            log.error( "Failed to fetch today's egg rate for city={}, state={}: {}", city, state, e.getMessage());

            throw new BadRequestException(
                    "Failed to fetch today's egg rate: "
                            + e.getMessage());
        }
    }

    // =========================================================
    // GET YESTERDAY'S EGG RATE FROM RAPIDAPI
    // =========================================================

    @Override
    public EggRateSingleResponseDto getYesterdayRate( String city, String state) {

        validateCityAndState(city, state);

        try {

            log.info( "Fetching yesterday's egg rate for city={}, state={}", city, state);

            EggRateSingleResponseDto response =
                    eggRateApiClient.getYesterdayRate( city.trim(), state.trim());

            log.info( "Yesterday's egg rate fetched successfully for city={}", city);

            return response;

        } catch (Exception e) {

            log.error( "Failed to fetch yesterday's egg rate for city={}, state={}: {}", city, state, e.getMessage());

            throw new BadRequestException(
                    "Failed to fetch yesterday's egg rate: "
                            + e.getMessage());
        }
    }

    // =========================================================
    // VALIDATION HELPER
    // =========================================================

    private void validateCityAndState( String city, String state) {

        if (city == null || city.isBlank()) {
            throw new BadRequestException(
                    "City is required");
        }

        if (state == null || state.isBlank()) {
            throw new BadRequestException(
                    "State is required");
        }
    }
}
