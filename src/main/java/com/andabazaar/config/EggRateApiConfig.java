package com.andabazaar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "rapidapi.egg-rates")
@Getter
@Setter
public class EggRateApiConfig {

    private String host;
    private String key;
}
