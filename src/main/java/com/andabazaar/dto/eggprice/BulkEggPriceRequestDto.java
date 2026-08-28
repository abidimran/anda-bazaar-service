package com.andabazaar.dto.eggprice;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkEggPriceRequestDto {

    @NotNull(message = "Price date is required")
    private LocalDate priceDate;

    @NotEmpty(message = "Price list cannot be empty")
    @Valid
    private List<EggPriceRequestDto> prices;
}