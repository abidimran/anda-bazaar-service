package com.andabazaar.mapper;

import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.repository.entity.EggPrice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EggPriceMapper {
    @Mapping(source = "market.id", target = "marketId")
    @Mapping(source = "market.name", target = "marketName")
    @Mapping(source = "market.city.id", target = "cityId")
    @Mapping(source = "market.city.name", target = "cityName")
    EggPriceResponseDto toResponseDto(EggPrice eggPrice);
}
