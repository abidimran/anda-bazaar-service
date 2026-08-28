package com.andabazaar.dto.market;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketResponseDto {

    private Long id;

    private String name;

    private Long cityId;

    private String cityName;

    private Long stateId;

    private String stateName;

    private String address;

    private String pincode;

    private String contactPerson;

    private String contactNumber;

    private Boolean active;
}