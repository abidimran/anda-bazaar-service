package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.FavoriteMarket;

public interface FavoriteMarketRepository
        extends JpaRepository<FavoriteMarket, Long> {

    List<FavoriteMarket> findByUserIdOrderByCreatedAtDesc( Long userId);

    Optional<FavoriteMarket> findByUserIdAndMarketId( Long userId, Long marketId);

    boolean existsByUserIdAndMarketId( Long userId, Long marketId);

    long countByUserId(Long userId);

    void deleteByUserIdAndMarketId( Long userId, Long marketId);
}