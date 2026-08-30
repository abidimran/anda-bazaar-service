package com.andabazaar.serviceimpl;

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.ExpectedPriceMapper;
import com.andabazaar.repository.ExpectedPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.entity.ExpectedPrice;
import com.andabazaar.repository.entity.Market;
import com.andabazaar.service.ExpectedPriceService;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpectedPriceServiceImpl implements ExpectedPriceService {
    private final ExpectedPriceRepository expectedPriceRepository;
    private final MarketRepository marketRepository;
    private final ExpectedPriceMapper expectedPriceMapper;

    @Override
    public ExpectedPriceResponseDto createExpectedPrice(ExpectedPriceRequestDto request) {
        Market market = findMarket(request.getMarketId());
        if (expectedPriceRepository
                .existsByMarketIdAndExpectedDate( request.getMarketId(), request.getExpectedDate())) {
            throw new BadRequestException("Expected price already exists for this market and date");
        }

        ExpectedPrice expectedPrice = ExpectedPrice.builder()
                .market(market)
                .expectedDate(request.getExpectedDate())
                .expectedPrice(request.getExpectedPrice())
                .reason(request.getReason())
                .active(true)
                .build();
        return expectedPriceMapper.toResponseDto( expectedPriceRepository.save(expectedPrice));
    }

    @Override
    public ExpectedPriceResponseDto updateExpectedPrice(Long id, ExpectedPriceRequestDto request) {
        ExpectedPrice expectedPrice = findExpectedPrice(id);
        Market market = findMarket(request.getMarketId());
        boolean marketChanged =
                !expectedPrice.getMarket().getId()
                        .equals(request.getMarketId());
        boolean dateChanged =
                !expectedPrice.getExpectedDate()
                        .equals(request.getExpectedDate());
        if (marketChanged || dateChanged) {
            if (expectedPriceRepository
                    .existsByMarketIdAndExpectedDate( request.getMarketId(), request.getExpectedDate())) {
                throw new BadRequestException("Expected price already exists for this market and date");
            }
        }

        expectedPrice.setMarket(market);
        expectedPrice.setExpectedDate( request.getExpectedDate());
        expectedPrice.setExpectedPrice( request.getExpectedPrice());
        expectedPrice.setReason( request.getReason());
        return expectedPriceMapper.toResponseDto( expectedPriceRepository.save(expectedPrice));
    }

    @Override
    @Transactional(readOnly = true)
    public ExpectedPriceResponseDto getExpectedPriceById(Long id) {
        return expectedPriceMapper.toResponseDto( findExpectedPrice(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto> getByMarket(Long marketId) {
        if (!marketRepository.existsById(marketId)) {
            throw new ResourceNotFoundException("Market not found with id: " + marketId);
        }

        return expectedPriceRepository
                .findByMarketIdOrderByExpectedDateDesc( marketId )
                .stream()
                .filter(ExpectedPrice::getActive)
                .map(expectedPriceMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExpectedPriceResponseDto getByMarketAndDate(Long marketId, LocalDate expectedDate) {
        ExpectedPrice expectedPrice =
                expectedPriceRepository
                        .findByMarketIdAndExpectedDate( marketId, expectedDate )
                        .orElseThrow(() -> new ResourceNotFoundException("Expected price not found for market and date"));
        if (!Boolean.TRUE.equals(
                expectedPrice.getActive())) {
            throw new ResourceNotFoundException("Expected price is not active");
        }

        return expectedPriceMapper.toResponseDto(expectedPrice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto>
            getActiveExpectedPrices() {
        return expectedPriceRepository
                .findByActiveTrueOrderByExpectedDateDesc()
                .stream()
                .map(expectedPriceMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return expectedPriceRepository
                .findByExpectedDateBetweenOrderByExpectedDateDesc( startDate, endDate )
                .stream()
                .filter(ExpectedPrice::getActive)
                .map(expectedPriceMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto>
            getMarketDateRange(Long marketId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        if (!marketRepository.existsById(marketId)) {
            throw new ResourceNotFoundException("Market not found with id: " + marketId);
        }

        return expectedPriceRepository
                .findByMarketIdAndExpectedDateBetweenOrderByExpectedDateDesc( marketId, startDate, endDate )
                .stream()
                .filter(ExpectedPrice::getActive)
                .map(expectedPriceMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteExpectedPrice(Long id) {
        ExpectedPrice expectedPrice = findExpectedPrice(id);
        expectedPrice.setActive(false);
        expectedPriceRepository.save(expectedPrice);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveExpectedPrices() {
        return expectedPriceRepository
                .countByActiveTrue();
    }

    private ExpectedPrice findExpectedPrice(Long id) {
        return expectedPriceRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expected price not found with id: " + id));
    }

    private Market findMarket(Long id) {
        return marketRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with id: " + id));
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BadRequestException("Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot be after end date");
        }
    }
}
