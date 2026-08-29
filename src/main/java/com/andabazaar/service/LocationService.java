package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.location.LocationRequestDto;
import com.andabazaar.dto.location.LocationResponseDto;

public interface LocationService {

    LocationResponseDto createLocation(LocationRequestDto request);

    LocationResponseDto getLocationById(Long id);

    List<LocationResponseDto> getAllLocations();

    LocationResponseDto updateLocation(Long id, LocationRequestDto request);

    void deleteLocation(Long id);

    List<LocationResponseDto> getRapidEnabledLocations();
}
