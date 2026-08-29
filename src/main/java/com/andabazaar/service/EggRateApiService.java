package com.andabazaar.service;

import com.andabazaar.dto.eggrate.EggRateApiResponseDto;
import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;

public interface EggRateApiService {
    EggRateApiResponseDto getEggRates( String city, String state);
    EggRateSingleResponseDto getTodayRate(String city, String state);
    EggRateSingleResponseDto getYesterdayRate( String city, String state);
}
