package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.EggPrice;
import com.andabazaar.repository.entity.Market;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.service.EggPriceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EggPriceServiceImpl implements EggPriceService {

    private final EggPriceRepository eggPriceRepository;
    private final MarketRepository marketRepository;

    @Override
    public EggPriceResponseDto createPrice(EggPriceRequestDto request) {

        Market market = findMarket(request.getMarketId());

        if (eggPriceRepository.existsByMarketIdAndPriceDate(
                request.getMarketId(),
                request.getPriceDate())) {

            throw new BadRequestException("Price already exists for this market and date");
        }

        BigDecimal previousPrice = getPreviousPrice(request.getMarketId(), request.getPriceDate());

        String priceChangeType = calculateChangeType(previousPrice, request.getPricePerEgg());

        BigDecimal priceChangeAmount = calculateChangeAmount(previousPrice, request.getPricePerEgg());

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

        return mapToResponse(eggPriceRepository.save(eggPrice));
    }

    @Override
    public EggPriceResponseDto updatePrice(Long id, EggPriceRequestDto request) {

        EggPrice eggPrice = findPrice(id);

        Market market = findMarket(request.getMarketId());

        if (!eggPrice.getMarket().getId().equals(request.getMarketId())
                || !eggPrice.getPriceDate().equals(request.getPriceDate())) {

            if (eggPriceRepository.existsByMarketIdAndPriceDate(
                    request.getMarketId(), request.getPriceDate())) {

                throw new BadRequestException("Price already exists for this market and date");
            }
        }

        BigDecimal previousPrice = getPreviousPrice(request.getMarketId(), request.getPriceDate());

        String priceChangeType = calculateChangeType(previousPrice, request.getPricePerEgg());

        BigDecimal priceChangeAmount = calculateChangeAmount(previousPrice, request.getPricePerEgg());

        eggPrice.setMarket(market);
        eggPrice.setPriceDate(request.getPriceDate());
        eggPrice.setPricePerEgg(request.getPricePerEgg());
        eggPrice.setPricePerTray(request.getPricePerTray());
        eggPrice.setPreviousPrice(previousPrice);
        eggPrice.setPriceChangeType(priceChangeType);
        eggPrice.setPriceChangeAmount(priceChangeAmount);
        eggPrice.setRemarks(request.getRemarks());

        return mapToResponse(eggPriceRepository.save(eggPrice));
    }

    @Override
    @Transactional(readOnly = true)
    public EggPriceResponseDto getPriceById(Long id) {
        return mapToResponse(findPrice(id));
    }

    @Override
    @Transactional(readOnly = true)
    public EggPriceResponseDto getMarketPrice(Long marketId, LocalDate date) {

        EggPrice price = eggPriceRepository
                .findByMarketIdAndPriceDate(marketId, date)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Egg price not found for market and date"));

        return mapToResponse(price);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getTodayPrices() {

        LocalDate today = LocalDate.now();

        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .filter(price -> price.getPriceDate().equals(today))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getYesterdayPrices() {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .filter(price -> price.getPriceDate().equals(yesterday))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getPriceHistory(Long marketId, LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        return eggPriceRepository
                .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(marketId, startDate, endDate)
                .stream()
                .filter(EggPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getUserPrices(Long userId) {

        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getUserPriceHistory(Long userId, Long marketId, LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        return eggPriceRepository
                .findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(marketId, startDate, endDate)
                .stream()
                .filter(EggPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deletePrice(Long id) {

        EggPrice eggPrice = findPrice(id);
        eggPrice.setActive(false);
        eggPriceRepository.save(eggPrice);
    }

    private EggPrice findPrice(Long id) {
        return eggPriceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Egg price not found with id: " + id));
    }

    private Market findMarket(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Market not found with id: " + id));
    }

    private BigDecimal getPreviousPrice(Long marketId, LocalDate date) {

        List<EggPrice> prices = eggPriceRepository.findByMarketIdOrderByPriceDateDesc(marketId);

        return prices.stream()
                .filter(price -> price.getPriceDate().isBefore(date))
                .findFirst()
                .map(EggPrice::getPricePerEgg)
                .orElse(null);
    }

    private String calculateChangeType(BigDecimal previousPrice, BigDecimal currentPrice) {
        if (previousPrice == null) {
            return "NO_CHANGE";
        }
        int result = currentPrice.compareTo(previousPrice);
        if (result > 0) return "INCREASE";
        if (result < 0) return "DECREASE";
        return "NO_CHANGE";
    }

    private BigDecimal calculateChangeAmount(BigDecimal previousPrice, BigDecimal currentPrice) {
        if (previousPrice == null) {
            return BigDecimal.ZERO;
        }
        return currentPrice.subtract(previousPrice);
    }

    private EggPriceResponseDto mapToResponse(EggPrice price) {

        Market market = price.getMarket();
        City city = market.getCity();

        return EggPriceResponseDto.builder()
                .id(price.getId())
                .marketId(market.getId())
                .marketName(market.getName())
                .cityId(city != null ? city.getId() : null)
                .cityName(city != null ? city.getName() : null)
                .priceDate(price.getPriceDate())
                .pricePerEgg(price.getPricePerEgg())
                .pricePerTray(price.getPricePerTray())
                .previousPrice(price.getPreviousPrice())
                .priceChangeType(price.getPriceChangeType())
                .priceChangeAmount(price.getPriceChangeAmount())
                .remarks(price.getRemarks())
                .active(price.getActive())
                .build();
    }
}
