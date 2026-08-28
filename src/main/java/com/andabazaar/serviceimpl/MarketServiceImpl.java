package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.market.MarketRequestDto;
import com.andabazaar.dto.market.MarketResponseDto;
import com.andabazaar.entity.City;
import com.andabazaar.entity.Market;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.service.MarketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;

    private final CityRepository cityRepository;

    @Override
    public MarketResponseDto createMarket( MarketRequestDto request) {

        City city = findCity(request.getCityId());

        if (marketRepository
                .existsByNameIgnoreCaseAndCityId( request.getName(), request.getCityId())) {

            throw new BadRequestException(
                    "Market already exists in this city");
        }

        Market market = Market.builder()
                .name(request.getName().trim())
                .city(city)
                .address(request.getAddress())
                .pincode(request.getPincode())
                .contactPerson( request.getContactPerson())
                .contactNumber( request.getContactNumber())
                .active(true)
                .build();

        return mapToResponse(
                marketRepository.save(market));
    }

    @Override
    @Transactional(readOnly = true)
    public MarketResponseDto getMarketById( Long id) {

        return mapToResponse(findMarket(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketResponseDto> getAllMarkets() {

        return marketRepository
                .findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketResponseDto> getMarketsByCity( Long cityId) {

        findCity(cityId);

        return marketRepository
                .findByCityIdAndActiveTrueOrderByNameAsc( cityId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MarketResponseDto updateMarket( Long id, MarketRequestDto request) {

        Market market = findMarket(id);

        City city = findCity( request.getCityId());

        boolean changed =
                !market.getName()
                        .equalsIgnoreCase(request.getName())
                || !market.getCity().getId()
                        .equals(request.getCityId());

        if (changed &&
                marketRepository
                    .existsByNameIgnoreCaseAndCityId( request.getName(), request.getCityId())) {

            throw new BadRequestException(
                    "Market already exists in this city");
        }

        market.setName(request.getName().trim());
        market.setCity(city);
        market.setAddress(request.getAddress());
        market.setPincode(request.getPincode());
        market.setContactPerson( request.getContactPerson());
        market.setContactNumber( request.getContactNumber());

        return mapToResponse(
                marketRepository.save(market));
    }

    @Override
    public void deleteMarket(Long id) {

        Market market = findMarket(id);

        market.setActive(false);

        marketRepository.save(market);
    }

    private Market findMarket(Long id) {

        return marketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Market not found with id: " + id));
    }

    private City findCity(Long id) {

        return cityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "City not found with id: " + id));
    }

    private MarketResponseDto mapToResponse( Market market) {

        City city = market.getCity();

        return MarketResponseDto.builder()
                .id(market.getId())
                .name(market.getName())
                .cityId(city.getId())
                .cityName(city.getName())
                .stateId(city.getState().getId())
                .stateName(city.getState().getName())
                .address(market.getAddress())
                .pincode(market.getPincode())
                .contactPerson( market.getContactPerson())
                .contactNumber( market.getContactNumber())
                .active(market.getActive())
                .build();
    }
}