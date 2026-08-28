package com.andabazaar.dto.location;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponseDto {

    private Long id;

    private String name;

    private Long stateId;

    private String stateName;

    private Boolean active;
}