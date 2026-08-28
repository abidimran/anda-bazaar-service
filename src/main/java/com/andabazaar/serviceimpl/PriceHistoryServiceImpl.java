package com.andabazaar.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.entity.PriceHistory;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.PriceHistoryRepository;
import com.andabazaar.service.PriceHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceHistoryServiceImpl
        implements PriceHistoryService {

    private final PriceHistoryRepository
            priceHistoryRepository;

    @Override
    public PriceHistory createPriceHistory( PriceHistory priceHistory) {

        if (priceHistory == null) {
            throw new BadRequestException(
                    "Price history cannot be null");
        }

        if (priceHistory.getMarket() == null
                || priceHistory.getMarket().getId() == null) {

            throw new BadRequestException(
                    "Market is required");
        }

        if (priceHistory.getPriceDate() == null) {

            throw new BadRequestException(
                    "Price date is required");
        }

        if (priceHistory.getPricePerEgg() == null) {

            throw new BadRequestException(
                    "Price per egg is required");
        }

        if (priceHistoryRepository
                .existsByMarketIdAndPriceDate( priceHistory.getMarket().getId(), priceHistory.getPriceDate()
                )) {

            throw new BadRequestException(
                    "Price history already exists for this market and date");
        }

        return priceHistoryRepository.save(
                priceHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public PriceHistory getById(Long id) {

        return priceHistoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Price history not found with id: "
                                        + id
                        ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceHistory> getByMarket( Long marketId) {

        return priceHistoryRepository
                .findByMarketIdOrderByPriceDateDesc( marketId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceHistory>
    getByMarketAndDateRange( Long marketId, LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {

            throw new BadRequestException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {

            throw new BadRequestException(
                    "Start date cannot be after end date");
        }

        return priceHistoryRepository
                .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc( marketId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceHistory> getByDate( LocalDate date) {

        if (date == null) {

            throw new BadRequestException(
                    "Date is required");
        }

        return priceHistoryRepository
                .findByPriceDateOrderByPriceDateDesc( date);
    }

    @Override
    public void delete(Long id) {

        PriceHistory priceHistory = getById(id);

        priceHistoryRepository.delete( priceHistory);
    }
}