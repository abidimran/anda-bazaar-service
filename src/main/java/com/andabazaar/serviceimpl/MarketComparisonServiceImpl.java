package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.comparison.MarketComparisonResponseDto;
import com.andabazaar.entity.City;
import com.andabazaar.entity.EggPrice;
import com.andabazaar.entity.Market;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.service.MarketComparisonService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketComparisonServiceImpl
        implements MarketComparisonService {

    private final MarketRepository marketRepository;
    private final EggPriceRepository eggPriceRepository;

    @Override
    public List<MarketComparisonResponseDto> compareMarkets() {

        return marketRepository.findAll()
                .stream()
                .filter(Market::getActive)
                .map(this::mapMarket)
                .toList();
    }

    @Override
    public MarketComparisonResponseDto compareMarket( Long marketId) {

        Market market = marketRepository.findById(marketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Market not found with id: "
                                        + marketId
                        ));

        return mapMarket(market);
    }

    private MarketComparisonResponseDto mapMarket( Market market) {

        List<EggPrice> prices =
                eggPriceRepository
                        .findByMarketIdOrderByPriceDateDesc( market.getId());

        List<BigDecimal> validPrices =
                prices.stream()
                        .filter(EggPrice::getActive)
                        .map(EggPrice::getPricePerEgg)
                        .filter(price -> price != null)
                        .toList();

        BigDecimal currentPrice = validPrices.isEmpty()
                ? BigDecimal.ZERO
                : validPrices.get(0);

        BigDecimal lowestPrice = validPrices.stream()
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal highestPrice = validPrices.stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal averagePrice = calculateAverage( validPrices);

        City city = market.getCity();

        return MarketComparisonResponseDto.builder()
                .marketId(market.getId())
                .marketName(market.getName())
                .cityName( city != null ? city.getName()
                                : null
                )
                .currentPrice(currentPrice)
                .lowestPrice(lowestPrice)
                .highestPrice(highestPrice)
                .averagePrice(averagePrice)
                .build();
    }

    private BigDecimal calculateAverage( List<BigDecimal> prices) {

        if (prices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = prices.stream()
                .reduce( BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf(prices.size()),
                2,
                java.math.RoundingMode.HALF_UP);
    }
}