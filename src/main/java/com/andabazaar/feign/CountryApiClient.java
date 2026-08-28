package com.andabazaar.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.andabazaar.dto.country.CountryRequest;
import com.andabazaar.dto.country.StateCityRequest;
import com.andabazaar.dto.country.StatesApiResponse;
import com.andabazaar.dto.country.CitiesApiResponse;

@FeignClient(name = "country-api", url = "https://countriesnow.space/api/v0.1")
public interface CountryApiClient {

    @PostMapping("/countries/states")
    StatesApiResponse getStates(@RequestBody CountryRequest request);

    @PostMapping("/countries/state/cities")
    CitiesApiResponse getCities(@RequestBody StateCityRequest request);
}
