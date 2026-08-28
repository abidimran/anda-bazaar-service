package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.favorite.FavoriteMarketResponseDto;
import com.andabazaar.entity.FavoriteMarket;

@Component
public class FavoriteMarketMapper {

    public FavoriteMarketResponseDto toDto(
            FavoriteMarket favoriteMarket) {

        if (favoriteMarket == null) {
            return null;
        }

        return FavoriteMarketResponseDto.builder()
                .id(favoriteMarket.getId())
                .userId(
                        favoriteMarket.getUser() != null
                                ? favoriteMarket.getUser().getId()
                                : null
                )
                .marketId(
                        favoriteMarket.getMarket() != null
                                ? favoriteMarket.getMarket().getId()
                                : null
                )
                .marketName(
                        favoriteMarket.getMarket() != null
                                ? favoriteMarket.getMarket().getName()
                                : null
                )
                .cityName(
                        favoriteMarket.getMarket() != null
                                && favoriteMarket.getMarket().getCity() != null
                                ? favoriteMarket.getMarket()
                                        .getCity()
                                        .getName()
                                : null
                )
                .createdAt(
                        favoriteMarket.getCreatedAt()
                )
                .build();
    }
}