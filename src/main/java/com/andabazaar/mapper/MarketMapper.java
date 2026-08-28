package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.market.MarketResponseDto;
import com.andabazaar.entity.Market;

@Component
public class MarketMapper {

    public MarketResponseDto toDto(Market market) {

        if (market == null) {
            return null;
        }

        MarketResponseDto dto = MarketResponseDto.builder()
                .id(market.getId())
                .name(market.getName())
                .address(market.getAddress())
                .pincode(market.getPincode())
                .contactPerson(market.getContactPerson())
                .contactNumber(market.getContactNumber())
                .active(market.getActive())
                .build();

        if (market.getCity() != null) {

            dto.setCityId(market.getCity().getId());
            dto.setCityName(market.getCity().getName());

            if (market.getCity().getState() != null) {
                dto.setStateId(
                        market.getCity().getState().getId()
                );

                dto.setStateName(
                        market.getCity()
                                .getState()
                                .getName()
                );
            }
        }

        return dto;
    }
}