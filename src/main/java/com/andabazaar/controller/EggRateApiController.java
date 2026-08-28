package com.andabazaar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.dto.eggrate.EggRateApiResponseDto;
import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;
import com.andabazaar.service.EggRateApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/egg-rates-external")
@RequiredArgsConstructor
public class EggRateApiController {

    private final EggRateApiService eggRateApiService;

    // =========================================================
    // GET ALL EGG RATES
    //
    // GET /api/egg-rates-external?city=Jaipur&state=Rajasthan
    // =========================================================

    @GetMapping
    public ResponseEntity<EggRateApiResponseDto> getEggRates(@RequestParam String city, @RequestParam String state) {

 return ResponseEntity.ok(eggRateApiService.getEggRates(city, state));
    }

    // =========================================================
    // GET TODAY'S EGG RATE
    //
    // GET /api/egg-rates-external/today?city=Jaipur&state=Rajasthan
    // =========================================================

    @GetMapping("/today")
    public ResponseEntity<EggRateSingleResponseDto> getTodayRate(@RequestParam String city, @RequestParam String state) {

 return ResponseEntity.ok(eggRateApiService.getTodayRate(city, state));
    }

    // =========================================================
    // GET YESTERDAY'S EGG RATE
    //
    // GET /api/egg-rates-external/yesterday?city=Jaipur&state=Rajasthan
    // =========================================================

    @GetMapping("/yesterday")
    public ResponseEntity<EggRateSingleResponseDto> getYesterdayRate(@RequestParam String city, @RequestParam String state) {

 return ResponseEntity.ok(eggRateApiService.getYesterdayRate(city, state));
    }
}
