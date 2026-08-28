package com.andabazaar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.entity.EggPrice;

@Mapper(componentModel = "spring")
public interface EggPriceMapper {

    @Mapping(source = "market.id", target = "marketId")
    @Mapping(source = "market.name", target = "marketName")
    @Mapping(source = "market.city.id", target = "cityId")
    @Mapping(source = "market.city.name", target = "cityName")
    @Mapping(source = "market.city.state.id", target = "stateId")
    @Mapping(source = "market.city.state.name", target = "stateName")
    EggPriceResponseDto toDto(EggPrice eggPrice);
}
