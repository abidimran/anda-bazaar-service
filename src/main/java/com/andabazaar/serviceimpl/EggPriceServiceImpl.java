package com.andabazaar.serviceimpl;

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.EggPriceMapper;
import com.andabazaar.repository.EggPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.entity.EggPrice;
import com.andabazaar.repository.entity.Market;
import com.andabazaar.service.EggPriceService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EggPriceServiceImpl implements EggPriceService {
    private final EggPriceRepository eggPriceRepository;
    private final MarketRepository marketRepository;
    private final EggPriceMapper eggPriceMapper;

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
        return eggPriceMapper.toResponseDto(eggPriceRepository.save(eggPrice));
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
        return eggPriceMapper.toResponseDto(eggPriceRepository.save(eggPrice));
    }

    @Override
    @Transactional(readOnly = true)
    public EggPriceResponseDto getPriceById(Long id) {
        return eggPriceMapper.toResponseDto(findPrice(id));
    }

    @Override
    @Transactional(readOnly = true)
    public EggPriceResponseDto getMarketPrice(Long marketId, LocalDate date) {
        EggPrice price = eggPriceRepository
                .findByMarketIdAndPriceDate(marketId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Egg price not found for market and date"));
        return eggPriceMapper.toResponseDto(price);
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
                .map(eggPriceMapper::toResponseDto)
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
                .map(eggPriceMapper::toResponseDto)
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
                .map(eggPriceMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EggPriceResponseDto> getUserPrices(Long userId) {
        return eggPriceRepository
                .findAll()
                .stream()
                .filter(EggPrice::getActive)
                .map(eggPriceMapper::toResponseDto)
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
                .map(eggPriceMapper::toResponseDto)
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
                .orElseThrow(() -> new ResourceNotFoundException("Egg price not found with id: " + id));
    }

    private Market findMarket(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with id: " + id));
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
}
