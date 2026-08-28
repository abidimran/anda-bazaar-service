package com.andabazaar.service;

import java.util.List;

import com.andabazaar.entity.FavoriteMarket;

public interface FavoriteMarketService {

    FavoriteMarket addFavorite( Long userId, Long marketId);

    void removeFavorite( Long userId, Long marketId);

    List<FavoriteMarket> getUserFavorites( Long userId);

    boolean isFavorite( Long userId, Long marketId);

    long countUserFavorites( Long userId);
}