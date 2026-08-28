package com.andabazaar.dto.country;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatesApiResponse {

    private Boolean error;
    private String msg;
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        private String name;
        private String iso3;
        private String iso2;
        private List<StateInfo> states;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StateInfo {
        private String name;

        @JsonProperty("state_code")
        private String stateCode;
    }
}
