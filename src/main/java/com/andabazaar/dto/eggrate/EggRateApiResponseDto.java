package com.andabazaar.dto.eggrate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EggRateApiResponseDto {

    private Boolean success;

    private String city;

    private String state;

    private String unit;

    @JsonProperty("today_date")
    private String todayDate;

    @JsonProperty("today_rate")
    private String todayRate;

    @JsonProperty("yesterday_rate")
    private String yesterdayRate;

    private String trend;

    private String change;

    private Stats stats;

    private List<DailyRate> rates;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Stats {

        @JsonProperty("total_records")
        private Integer totalRecords;

        @JsonProperty("highest_rate")
        private String highestRate;

        @JsonProperty("lowest_rate")
        private String lowestRate;

        @JsonProperty("average_rate")
        private String averageRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyRate {

        private String date;

        private String rate;
    }
}
