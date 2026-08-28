package com.andabazaar.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.andabazaar.config.EggRateFeignConfig;
import com.andabazaar.dto.eggrate.EggRateApiResponseDto;
import com.andabazaar.dto.eggrate.EggRateSingleResponseDto;

@FeignClient(
        name = "egg-rate-api",
        url = "${rapidapi.egg-rates.base-url}",
        configuration = EggRateFeignConfig.class
)
public interface EggRateApiClient {

    @GetMapping("/api/Today-Egg-Rates/all_rates.php")
    EggRateApiResponseDto getEggRates(@RequestParam("city") String city, @RequestParam("state") String state);

    @GetMapping("/api/Today-Egg-Rates/today_rate.php")
    EggRateSingleResponseDto getTodayRate(@RequestParam("city") String city, @RequestParam("state") String state);

    @GetMapping("/api/Today-Egg-Rates/yesterday_rate.php")
    EggRateSingleResponseDto getYesterdayRate(@RequestParam("city") String city, @RequestParam("state") String state);
}
