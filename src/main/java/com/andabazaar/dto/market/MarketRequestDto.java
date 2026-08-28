package com.andabazaar.dto.market;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketRequestDto {

    @NotBlank(message = "Market name is required")
    @Size(max = 150)
    private String name;

    @NotNull(message = "City ID is required")
    private Long cityId;

    @Size(max = 500)
    private String address;

    @Size(max = 20)
    private String pincode;

    @Size(max = 100)
    private String contactPerson;

    @Size(max = 20)
    private String contactNumber;
}