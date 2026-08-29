package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.location.LocationRequestDto;
import com.andabazaar.dto.location.LocationResponseDto;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.LocationMapper;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.CountryRepository;
import com.andabazaar.repository.LocationRepository;
import com.andabazaar.repository.StateRepository;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.Country;
import com.andabazaar.repository.entity.Location;
import com.andabazaar.repository.entity.State;
import com.andabazaar.service.LocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationResponseDto createLocation(LocationRequestDto request) {
        Country country = findOrCreateCountry(request.getCountryName().trim());
        State state = findOrCreateState(request.getStateName().trim());
        City city = findOrCreateCity(request.getCityName().trim());

        Location location = Location.builder()
                .country(country)
                .state(state)
                .city(city)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .rapidEnabled(request.isRapidEnabled())
                .build();

        return locationMapper.toResponseDto(locationRepository.save(location));
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponseDto getLocationById(Long id) {
        return locationMapper.toResponseDto(findLocation(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponseDto> getAllLocations() {
        return locationRepository.findAll().stream().map(locationMapper::toResponseDto).toList();
    }

    @Override
    public LocationResponseDto updateLocation(Long id, LocationRequestDto request) {
        Location location = findLocation(id);

        Country country = findOrCreateCountry(request.getCountryName().trim());
        State state = findOrCreateState(request.getStateName().trim());
        City city = findOrCreateCity(request.getCityName().trim());

        location.setCountry(country);
        location.setState(state);
        location.setCity(city);
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setRapidEnabled(request.isRapidEnabled());

        return locationMapper.toResponseDto(locationRepository.save(location));
    }

    @Override
    public void deleteLocation(Long id) {
        Location location = findLocation(id);
        locationRepository.delete(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponseDto> getRapidEnabledLocations() {
        return locationRepository.findByRapidEnabledTrue().stream().map(locationMapper::toResponseDto).toList();
    }

    private Country findOrCreateCountry(String name) {
        return countryRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            Country country = Country.builder().name(name).build();
            return countryRepository.save(country);
        });
    }

    private State findOrCreateState(String name) {
        return stateRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            State state = State.builder().name(name).build();
            return stateRepository.save(state);
        });
    }

    private City findOrCreateCity(String name) {
        return cityRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            City city = City.builder().name(name).build();
            return cityRepository.save(city);
        });
    }

    private Location findLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }
}
