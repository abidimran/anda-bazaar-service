package com.andabazaar.dto.country;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateCityRequest {

    private String country;
    private String state;
}
