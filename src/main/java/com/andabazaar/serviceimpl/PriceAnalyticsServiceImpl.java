package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.analytics.MarketStatisticsDto;
import com.andabazaar.dto.analytics.PriceAnalyticsResponseDto;
import com.andabazaar.dto.analytics.PriceTrendResponseDto;
import com.andabazaar.entity.EggPrice;
import com.andabazaar.entity.Market;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.service.PriceAnalyticsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriceAnalyticsServiceImpl
        implements PriceAnalyticsService {

    private final EggPriceRepository eggPriceRepository;
    private final MarketRepository marketRepository;

    @Override
    public PriceAnalyticsResponseDto getMarketAnalytics( Long marketId, LocalDate startDate, LocalDate endDate) {

        validateDates(startDate, endDate);

        Market market = findMarket(marketId);

        List<EggPrice> prices =
                eggPriceRepository
                        .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc( marketId, startDate, endDate )
                        .stream()
                        .filter(EggPrice::getActive)
                        .toList();

        if (prices.isEmpty()) {
            return PriceAnalyticsResponseDto.builder()
                    .marketId(market.getId())
                    .marketName(market.getName())
                    .startDate(startDate)
                    .endDate(endDate)
                    .lowestPrice(BigDecimal.ZERO)
                    .highestPrice(BigDecimal.ZERO)
                    .averagePrice(BigDecimal.ZERO)
                    .currentPrice(BigDecimal.ZERO)
                    .priceChange(BigDecimal.ZERO)
                    .priceChangePercentage(BigDecimal.ZERO)
                    .build();
        }

        BigDecimal lowest =
                getLowestPrice(prices);

        BigDecimal highest =
                getHighestPrice(prices);

        BigDecimal average =
                getAveragePrice(prices);

        // Descending list ka first = latest price
        BigDecimal currentPrice =
                prices.get(0).getPricePerEgg();

        BigDecimal oldestPrice =
                prices.get(prices.size() - 1)
                        .getPricePerEgg();

        BigDecimal priceChange =
                currentPrice.subtract(oldestPrice);

        BigDecimal percentage =
                calculatePercentageChange( oldestPrice, currentPrice);

        return PriceAnalyticsResponseDto.builder()
                .marketId(market.getId())
                .marketName(market.getName())
                .startDate(startDate)
                .endDate(endDate)
                .lowestPrice(lowest)
                .highestPrice(highest)
                .averagePrice(average)
                .currentPrice(currentPrice)
                .priceChange(priceChange)
                .priceChangePercentage(percentage)
                .build();
    }

    @Override
    public MarketStatisticsDto getMarketStatistics( Long marketId) {

        Market market = findMarket(marketId);

        List<EggPrice> prices =
                eggPriceRepository
                        .findByMarketIdOrderByPriceDateDesc( marketId )
                        .stream()
                        .filter(EggPrice::getActive)
                        .toList();

        BigDecimal lowest =
                getLowestPrice(prices);

        BigDecimal highest =
                getHighestPrice(prices);

        BigDecimal average =
                getAveragePrice(prices);

        BigDecimal current =
                prices.isEmpty()
                        ? BigDecimal.ZERO
                        : prices.get(0).getPricePerEgg();

        return MarketStatisticsDto.builder()
                .marketId(market.getId())
                .marketName(market.getName())
                .lowestPrice(lowest)
                .highestPrice(highest)
                .averagePrice(average)
                .currentPrice(current)
                .totalPriceRecords( (long) prices.size()
                )
                .build();
    }

    @Override
    public List<PriceTrendResponseDto> getPriceTrend( Long marketId, LocalDate startDate, LocalDate endDate) {

        validateDates(startDate, endDate);

        findMarket(marketId);

        List<EggPrice> prices =
                eggPriceRepository
                        .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc( marketId, startDate, endDate )
                        .stream()
                        .filter(EggPrice::getActive)
                        .toList();

        return prices.stream()
                .map(this::mapTrend)
                .toList();
    }

    @Override
    public List<MarketStatisticsDto>
    getAllMarketStatistics() {

        return marketRepository.findAll()
                .stream()
                .filter(Market::getActive)
                .map(market ->
                        getMarketStatistics( market.getId() )
                )
                .toList();
    }

    private PriceTrendResponseDto mapTrend( EggPrice price) {

        BigDecimal change =
                price.getPriceChangeAmount();

        if (change == null) {
            change = BigDecimal.ZERO;
        }

        BigDecimal percentage =
                BigDecimal.ZERO;

        BigDecimal previous =
                price.getPreviousPrice();

        if (previous != null
                && previous.compareTo( BigDecimal.ZERO) != 0) {

            percentage =
                    change.multiply( BigDecimal.valueOf(100) ).divide( previous, 2, RoundingMode.HALF_UP);
        }

        return PriceTrendResponseDto.builder()
                .priceDate(price.getPriceDate())
                .price(price.getPricePerEgg())
                .change(change)
                .changePercentage(percentage)
                .build();
    }

    private BigDecimal getLowestPrice( List<EggPrice> prices) {

        return prices.stream()
                .map(EggPrice::getPricePerEgg)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getHighestPrice( List<EggPrice> prices) {

        return prices.stream()
                .map(EggPrice::getPricePerEgg)
                .filter(price -> price != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getAveragePrice( List<EggPrice> prices) {

        List<BigDecimal> validPrices =
                prices.stream()
                        .map(EggPrice::getPricePerEgg)
                        .filter(price -> price != null)
                        .toList();

        if (validPrices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total =
                validPrices.stream()
                        .reduce( BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf( validPrices.size()
                ),
                2,
                RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePercentageChange( BigDecimal oldPrice, BigDecimal newPrice) {

        if (oldPrice == null
                || newPrice == null
                || oldPrice.compareTo( BigDecimal.ZERO) == 0) {

            return BigDecimal.ZERO;
        }

        return newPrice
                .subtract(oldPrice)
                .multiply(BigDecimal.valueOf(100))
                .divide( oldPrice, 2, RoundingMode.HALF_UP);
    }

    private Market findMarket(Long marketId) {

        return marketRepository.findById(marketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Market not found with id: "
                                        + marketId
                        ));
    }

    private void validateDates( LocalDate startDate, LocalDate endDate) {

        if (startDate == null
                || endDate == null) {

            throw new BadRequestException("Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {

            throw new BadRequestException("Start date cannot be after end date");
        }
    }
}