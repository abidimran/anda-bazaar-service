package com.andabazaar.controller;

import com.andabazaar.dto.eggrate.EggRateApiResponseDto;
import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;
import com.andabazaar.service.EggRateApiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "External Egg Rates", description = "Fetch egg rates from external RapidAPI")
@RestController
@RequestMapping("/api/eggRatesExternal")
@RequiredArgsConstructor
public class EggRateApiController {
    private final EggRateApiService eggRateApiService;
    // GET ALL EGG RATES
    //
    @Operation(summary = "Get Egg Rates")
    @GetMapping
    public ResponseEntity<EggRateApiResponseDto> getEggRates(@RequestParam String city, @RequestParam String state) {
 return ResponseEntity.ok(eggRateApiService.getEggRates(city, state));
    }

    // GET TODAY'S EGG RATE
    //
    @Operation(summary = "Get Today Rate")
    @GetMapping("/today")
    public ResponseEntity<EggRateSingleResponseDto> getTodayRate(@RequestParam String city, @RequestParam String state) {
 return ResponseEntity.ok(eggRateApiService.getTodayRate(city, state));
    }

    // GET YESTERDAY'S EGG RATE
    //
    @Operation(summary = "Get Yesterday Rate")
    @GetMapping("/yesterday")
    public ResponseEntity<EggRateSingleResponseDto> getYesterdayRate(@RequestParam String city, @RequestParam String state) {
 return ResponseEntity.ok(eggRateApiService.getYesterdayRate(city, state));
    }
}
