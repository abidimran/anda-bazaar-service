package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.location.CityRequestDto;
import com.andabazaar.dto.location.CityResponseDto;

public interface CityService {

    CityResponseDto createCity(CityRequestDto request);

    CityResponseDto getCityById(Long id);

    List<CityResponseDto> getAllCities();

    CityResponseDto updateCity(Long id, CityRequestDto request);

    void deleteCity(Long id);
}
