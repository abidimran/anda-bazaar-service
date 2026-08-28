package com.andabazaar.service;

import java.time.LocalDate;
import java.util.List;

import com.andabazaar.entity.PriceHistory;

public interface PriceHistoryService {

    PriceHistory createPriceHistory( PriceHistory priceHistory);

    PriceHistory getById( Long id);

    List<PriceHistory> getByMarket( Long marketId);

    List<PriceHistory> getByMarketAndDateRange( Long marketId, LocalDate startDate, LocalDate endDate);

    List<PriceHistory> getByDate( LocalDate date);

    void delete( Long id);
}