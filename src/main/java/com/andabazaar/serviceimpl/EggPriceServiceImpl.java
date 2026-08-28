package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.entity.City;
import com.andabazaar.entity.EggPrice;
import com.andabazaar.entity.Market;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.service.EggPriceService;
import com.andabazaar.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EggPriceServiceImpl implements EggPriceService {

    private final EggPriceRepository eggPriceRepository;
    private final MarketRepository marketRepository;

    // Subscription service required for user price access
    private final SubscriptionService subscriptionService;

    // =========================================================
    // ADMIN - CREATE PRICE
    // =========================================================

    @Override
    public EggPriceResponseDto createPrice(
            EggPriceRequestDto request) {

        Market market = findMarket(request.getMarketId());

        if (eggPriceRepository.existsByMarketIdAndPriceDate(
                request.getMarketId(),
                request.getPriceDate())) {

            throw new BadRequestException(
                    "Price already exists for this market and date");
        }

        BigDecimal previousPrice = getPreviousPrice(
                request.getMarketId(),
                request.getPriceDate());

        String priceChangeType = calculateChangeType(
                previousPrice,
                request.getPricePerEgg());

        BigDecimal priceChangeAmount =
                calculateChangeAmount(
                        previousPrice,
                        request.getPricePerEgg());

        EggPrice eggPrice = EggPrice.builder()
                .market(market)
                .priceDate(request.getPriceDate())
                .pricePerEgg(request.getPricePerEgg())
                .pricePerTray(request.getPricePerTray())
                .previousPrice(previousPrice)
                .priceChangeType(priceChangeType)
                .priceChangeAmount(priceChangeAmount)
                .remarks(request.getRemarks())
                .active(true)
                .build();

        return mapToResponse(
                eggPriceRepository.save(eggPrice));
    }

    // =========================================================
    // ADMIN - UPDATE PRICE
    // =========================================================

    @Override
    public EggPriceResponseDto updatePrice(
            Long id,
            EggPriceRequestDto request) {

        EggPrice eggPrice = findPrice(id);

        Market market = findMarket(
                request.getMarketId());

        if (!eggPrice.getMarket().getId()
                .equals(request.getMarketId())
                || !eggPrice.getPriceDate()
                        .equals(request.getPriceDate())) {

            if (eggPriceRepository
                    .existsByMarketIdAndPriceDate(
                            request.getMarketId(),
                            request.getPriceDate())) {

                throw new BadRequestException(
                        "Price already exists for this market and date");
            }
        }

        BigDecimal previousPrice = getPreviousPrice(
                request.getMarketId(),
                request.getPriceDate());

        String priceChangeType = calculateChangeType(
                previousPrice,
                request.getPricePerEgg());

        BigDecimal priceChangeAmount =
                calculateChangeAmount(
                        previousPrice,
                        request.getPricePerEgg());

        eggPrice.setMarket(market);
        eggPrice.setPriceDate(request.getPriceDate());
        eggPrice.setPricePerEgg(request.getPricePerEgg());
        eggPrice.setPricePerTray(request.getPricePerTray());
        eggPrice.setPreviousPrice(previousPrice);
        eggPrice.setPriceChangeType(priceChangeType);
        eggPrice.setPriceChangeAmount(priceChangeAmount);
        eggPrice.setRemarks(request.getRemarks());

        return mapToResponse(
                eggPriceRepository.save(eggPrice));
    }

    // =========================================================
    // GET PRICE BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public EggPriceResponseDto getPriceById(Long id) {

        return mapToResponse(findPrice(id));
    }

    // =========================================================
    // GET MARKET PRICE BY DATE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public EggPriceResponseDto getMarketPrice(
            Long marketId,
            LocalDate date) {

        EggPrice price = eggPriceRepository
                .findByMarketIdAndPriceDate(
                        marketId,
                        date)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Egg price not found for market and date"));

        return mapToResponse(price);
    }

    // =========================================================
    // TODAY PRICES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getTodayPrices() {

        LocalDate today = LocalDate.now();

        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .filter(price ->
                        price.getPriceDate()
                                .equals(today))
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // YESTERDAY PRICES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getYesterdayPrices() {

        LocalDate yesterday =
                LocalDate.now().minusDays(1);

        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .filter(price ->
                        price.getPriceDate()
                                .equals(yesterday))
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // PRICE HISTORY - PUBLIC/ADMIN
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getPriceHistory(
            Long marketId,
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {

            throw new BadRequestException(
                    "Start date cannot be after end date");
        }

        return eggPriceRepository
                .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(
                        marketId,
                        startDate,
                        endDate)
                .stream()
                .filter(EggPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // USER PRICE ACCESS
    //
    // ACTIVE SUBSCRIPTION:
    // Today + Yesterday + Older
    //
    // NO/EXPIRED SUBSCRIPTION:
    // Only 2 days old and older
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getUserPrices(
            Long userId) {

        boolean subscribed =
                subscriptionService.hasActiveSubscription(userId);

        LocalDate today = LocalDate.now();

        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .filter(price -> {

                    if (subscribed) {
                        return true;
                    }

                    /*
                     * Without active subscription:
                     *
                     * Today      -> BLOCK
                     * Yesterday  -> BLOCK
                     * 2 days old -> ALLOW
                     * Older      -> ALLOW
                     */

                    return !price.getPriceDate()
                            .isAfter(today.minusDays(2));
                })
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // USER PRICE HISTORY
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getUserPriceHistory(
            Long userId,
            Long marketId,
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {

            throw new BadRequestException(
                    "Start date cannot be after end date");
        }

        boolean subscribed =
                subscriptionService.hasActiveSubscription(userId);

        LocalDate today = LocalDate.now();

        /*
         * Non-subscribed user cannot request:
         *
         * Today
         * Yesterday
         */

        if (!subscribed) {

            LocalDate maximumAllowedDate =
                    today.minusDays(2);

            if (endDate.isAfter(maximumAllowedDate)) {

                throw new BadRequestException(
                        "Active subscription is required to view today and yesterday prices");
            }

            if (startDate.isAfter(maximumAllowedDate)) {

                throw new BadRequestException(
                        "Active subscription is required to view recent prices");
            }

            endDate = maximumAllowedDate;
        }

        return eggPriceRepository
                .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(
                        marketId,
                        startDate,
                        endDate)
                .stream()
                .filter(EggPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // DELETE PRICE - SOFT DELETE
    // =========================================================

    @Override
    public void deletePrice(Long id) {

        EggPrice eggPrice = findPrice(id);

        eggPrice.setActive(false);

        eggPriceRepository.save(eggPrice);
    }

    // =========================================================
    // FIND PRICE
    // =========================================================

    private EggPrice findPrice(Long id) {

        return eggPriceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Egg price not found with id: " + id));
    }

    // =========================================================
    // FIND MARKET
    // =========================================================

    private Market findMarket(Long id) {

        return marketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Market not found with id: " + id));
    }

    // =========================================================
    // PREVIOUS PRICE
    // =========================================================

    private BigDecimal getPreviousPrice(
            Long marketId,
            LocalDate date) {

        List<EggPrice> prices =
                eggPriceRepository
                        .findByMarketIdOrderByPriceDateDesc(
                                marketId);

        return prices.stream()
                .filter(price ->
                        price.getPriceDate()
                                .isBefore(date))
                .findFirst()
                .map(EggPrice::getPricePerEgg)
                .orElse(null);
    }

    // =========================================================
    // PRICE CHANGE TYPE
    // =========================================================

    private String calculateChangeType(
            BigDecimal previousPrice,
            BigDecimal currentPrice) {

        if (previousPrice == null) {
            return "NO_CHANGE";
        }

        int result =
                currentPrice.compareTo(previousPrice);

        if (result > 0) {
            return "INCREASE";
        }

        if (result < 0) {
            return "DECREASE";
        }

        return "NO_CHANGE";
    }

    // =========================================================
    // PRICE CHANGE AMOUNT
    // =========================================================

    private BigDecimal calculateChangeAmount(
            BigDecimal previousPrice,
            BigDecimal currentPrice) {

        if (previousPrice == null) {
            return BigDecimal.ZERO;
        }

        return currentPrice.subtract(previousPrice);
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private EggPriceResponseDto mapToResponse(
            EggPrice price) {

        Market market = price.getMarket();

        City city = market.getCity();

        return EggPriceResponseDto.builder()
                .id(price.getId())
                .marketId(market.getId())
                .marketName(market.getName())
                .cityId(city.getId())
                .cityName(city.getName())
                .stateId(city.getState().getId())
                .stateName(city.getState().getName())
                .priceDate(price.getPriceDate())
                .pricePerEgg(price.getPricePerEgg())
                .pricePerTray(price.getPricePerTray())
                .previousPrice(price.getPreviousPrice())
                .priceChangeType(
                        price.getPriceChangeType())
                .priceChangeAmount(
                        price.getPriceChangeAmount())
                .remarks(price.getRemarks())
                .active(price.getActive())
                .build();
    }
}