package com.andabazaar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.repository.entity.ExpectedPrice;

@Mapper(componentModel = "spring")
public interface ExpectedPriceMapper {

    @Mapping(source = "market.id", target = "marketId")
    @Mapping(source = "market.name", target = "marketName")
    @Mapping(source = "market.city.name", target = "cityName")
    ExpectedPriceResponseDto toResponseDto(ExpectedPrice expectedPrice);
}
