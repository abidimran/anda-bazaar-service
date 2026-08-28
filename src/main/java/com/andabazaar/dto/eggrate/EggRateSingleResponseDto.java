package com.andabazaar.dto.eggrate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EggRateSingleResponseDto {

    private Boolean success;

    private String city;

    private String state;

    private String date;

    private String rate;

    private String unit;

    @JsonProperty("today_rate")
    private String todayRate;

    private String trend;

    private String change;
}
