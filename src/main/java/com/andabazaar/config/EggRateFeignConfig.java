package com.andabazaar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class EggRateFeignConfig {

    private final EggRateApiConfig eggRateApiConfig;

    @Bean
    public RequestInterceptor eggRateApiRequestInterceptor() {

        return requestTemplate -> {

            requestTemplate.header( "Content-Type", "application/json");

            requestTemplate.header( "x-rapidapi-host", eggRateApiConfig.getHost());

            requestTemplate.header( "x-rapidapi-key", eggRateApiConfig.getKey());
        };
    }
}
