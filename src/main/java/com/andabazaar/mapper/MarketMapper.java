package com.andabazaar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andabazaar.dto.market.MarketResponseDto;
import com.andabazaar.entity.Market;

@Mapper(componentModel = "spring")
public interface MarketMapper {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "city.state.id", target = "stateId")
    @Mapping(source = "city.state.name", target = "stateName")
    MarketResponseDto toDto(Market market);
}
