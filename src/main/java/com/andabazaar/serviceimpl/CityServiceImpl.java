package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.location.CityRequestDto;
import com.andabazaar.dto.location.CityResponseDto;
import com.andabazaar.entity.City;
import com.andabazaar.entity.State;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.StateRepository;
import com.andabazaar.service.CityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private final StateRepository stateRepository;

    @Override
    public CityResponseDto createCity(
            CityRequestDto request) {

        State state = findState(request.getStateId());

        if (cityRepository
                .existsByNameIgnoreCaseAndStateId(
                        request.getName(),
                        request.getStateId())) {

            throw new BadRequestException(
                    "City already exists in this state");
        }

        City city = City.builder()
                .name(request.getName().trim())
                .state(state)
                .active(true)
                .build();

        return mapToResponse(
                cityRepository.save(city));
    }

    @Override
    @Transactional(readOnly = true)
    public CityResponseDto getCityById(Long id) {

        return mapToResponse(findCity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityResponseDto> getAllCities() {

        return cityRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityResponseDto> getCitiesByState(
            Long stateId) {

        findState(stateId);

        return cityRepository
                .findByStateIdAndActiveTrueOrderByNameAsc(
                        stateId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CityResponseDto updateCity(
            Long id,
            CityRequestDto request) {

        City city = findCity(id);

        State state = findState(
                request.getStateId());

        boolean changed =
                !city.getName()
                        .equalsIgnoreCase(request.getName())
                || !city.getState().getId()
                        .equals(request.getStateId());

        if (changed &&
                cityRepository
                    .existsByNameIgnoreCaseAndStateId(
                        request.getName(),
                        request.getStateId())) {

            throw new BadRequestException(
                    "City already exists in this state");
        }

        city.setName(request.getName().trim());
        city.setState(state);

        return mapToResponse(
                cityRepository.save(city));
    }

    @Override
    public void deleteCity(Long id) {

        City city = findCity(id);

        city.setActive(false);

        cityRepository.save(city);
    }

    private City findCity(Long id) {

        return cityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "City not found with id: " + id));
    }

    private State findState(Long id) {

        return stateRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "State not found with id: " + id));
    }

    private CityResponseDto mapToResponse(
            City city) {

        return CityResponseDto.builder()
                .id(city.getId())
                .name(city.getName())
                .stateId(city.getState().getId())
                .stateName(city.getState().getName())
                .active(city.getActive())
                .build();
    }
}