package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.entity.FavoriteMarket;
import com.andabazaar.entity.Market;
import com.andabazaar.entity.User;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.FavoriteMarketRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.FavoriteMarketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteMarketServiceImpl
        implements FavoriteMarketService {

    private final FavoriteMarketRepository favoriteMarketRepository;
    private final UserRepository userRepository;
    private final MarketRepository marketRepository;

    @Override
    public FavoriteMarket addFavorite( Long userId, Long marketId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId
                        ));

        Market market = marketRepository.findById(marketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Market not found with id: " + marketId
                        ));

        if (favoriteMarketRepository
                .existsByUserIdAndMarketId(userId, marketId)) {

            throw new RuntimeException("Market is already in favorites");
        }

        FavoriteMarket favorite =
                FavoriteMarket.builder()
                        .user(user)
                        .market(market)
                        .build();

        return favoriteMarketRepository.save(favorite);
    }

    @Override
    public void removeFavorite( Long userId, Long marketId) {

        if (!favoriteMarketRepository
                .existsByUserIdAndMarketId( userId, marketId )) {

            throw new ResourceNotFoundException("Favorite market not found");
        }

        favoriteMarketRepository
                .deleteByUserIdAndMarketId( userId, marketId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteMarket> getUserFavorites( Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return favoriteMarketRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite( Long userId, Long marketId) {

        return favoriteMarketRepository
                .existsByUserIdAndMarketId( userId, marketId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUserFavorites( Long userId) {

        return favoriteMarketRepository
                .countByUserId(userId);
    }
}