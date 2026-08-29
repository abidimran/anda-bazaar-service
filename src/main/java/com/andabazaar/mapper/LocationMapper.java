package com.andabazaar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andabazaar.dto.location.LocationResponseDto;
import com.andabazaar.repository.entity.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(source = "country.name", target = "countryName")
    @Mapping(source = "state.name", target = "stateName")
    @Mapping(source = "city.name", target = "cityName")
    LocationResponseDto toResponseDto(Location location);
}
