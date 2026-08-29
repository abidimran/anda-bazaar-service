package com.andabazaar.dto.location;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequestDto {
    @NotBlank(message = "Country name is required")
    private String countryName;

    @NotBlank(message = "State name is required")
    private String stateName;

    @NotBlank(message = "City name is required")
    private String cityName;

    private double latitude;
    private double longitude;
    private boolean rapidEnabled;
}
