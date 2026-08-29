package com.andabazaar.mapper;

import org.mapstruct.Mapper;

import com.andabazaar.dto.location.CityResponseDto;
import com.andabazaar.repository.entity.City;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityResponseDto toResponseDto(City city);
}
