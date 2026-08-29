package com.andabazaar.dto.location;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponseDto {

    private Long id;
    private String countryName;
    private String stateName;
    private String cityName;
    private double latitude;
    private double longitude;
    private boolean rapidEnabled;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
