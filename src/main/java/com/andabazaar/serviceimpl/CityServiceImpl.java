package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.location.CityRequestDto;
import com.andabazaar.dto.location.CityResponseDto;
import com.andabazaar.repository.entity.City;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.service.CityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public CityResponseDto createCity(CityRequestDto request) {

        if (cityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("City already exists");
        }

        City city = City.builder()
                .name(request.getName().trim())
                .build();

        return mapToResponse(cityRepository.save(city));
    }

    @Override
    @Transactional(readOnly = true)
    public CityResponseDto getCityById(Long id) {
        return mapToResponse(findCity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityResponseDto> getAllCities() {
        return cityRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CityResponseDto updateCity(Long id, CityRequestDto request) {

        City city = findCity(id);

        boolean nameChanged = !city.getName().equalsIgnoreCase(request.getName());

        if (nameChanged && cityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("City already exists");
        }

        city.setName(request.getName().trim());

        return mapToResponse(cityRepository.save(city));
    }

    @Override
    public void deleteCity(Long id) {
        City city = findCity(id);
        cityRepository.delete(city);
    }

    private City findCity(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found with id: " + id));
    }

    private CityResponseDto mapToResponse(City city) {
        return CityResponseDto.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }
}
