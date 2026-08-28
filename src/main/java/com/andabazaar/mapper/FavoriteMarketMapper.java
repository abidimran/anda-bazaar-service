package com.andabazaar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andabazaar.dto.favorite.FavoriteMarketResponseDto;
import com.andabazaar.entity.FavoriteMarket;

@Mapper(componentModel = "spring")
public interface FavoriteMarketMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "market.id", target = "marketId")
    @Mapping(source = "market.name", target = "marketName")
    @Mapping(source = "market.city.name", target = "cityName")
    FavoriteMarketResponseDto toDto(FavoriteMarket favoriteMarket);
}
