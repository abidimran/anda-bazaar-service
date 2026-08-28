package com.andabazaar.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.entity.City;
import com.andabazaar.entity.ExpectedPrice;
import com.andabazaar.entity.Market;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.ExpectedPriceRepository;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.service.ExpectedPriceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpectedPriceServiceImpl
        implements ExpectedPriceService {

    private final ExpectedPriceRepository expectedPriceRepository;
    private final MarketRepository marketRepository;

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public ExpectedPriceResponseDto createExpectedPrice( ExpectedPriceRequestDto request) {

        Market market = findMarket(request.getMarketId());

        if (expectedPriceRepository
                .existsByMarketIdAndExpectedDate( request.getMarketId(), request.getExpectedDate())) {

            throw new BadRequestException(
                    "Expected price already exists for this market and date");
        }

        ExpectedPrice expectedPrice = ExpectedPrice.builder()
                .market(market)
                .expectedDate(request.getExpectedDate())
                .expectedPrice(request.getExpectedPrice())
                .reason(request.getReason())
                .active(true)
                .build();

        return mapToResponse(
                expectedPriceRepository.save(expectedPrice));
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public ExpectedPriceResponseDto updateExpectedPrice( Long id, ExpectedPriceRequestDto request) {

        ExpectedPrice expectedPrice =
                findExpectedPrice(id);

        Market market =
                findMarket(request.getMarketId());

        boolean marketChanged =
                !expectedPrice.getMarket().getId()
                        .equals(request.getMarketId());

        boolean dateChanged =
                !expectedPrice.getExpectedDate()
                        .equals(request.getExpectedDate());

        if (marketChanged || dateChanged) {

            if (expectedPriceRepository
                    .existsByMarketIdAndExpectedDate( request.getMarketId(), request.getExpectedDate())) {

                throw new BadRequestException(
                        "Expected price already exists for this market and date");
            }
        }

        expectedPrice.setMarket(market);
        expectedPrice.setExpectedDate( request.getExpectedDate());
        expectedPrice.setExpectedPrice( request.getExpectedPrice());
        expectedPrice.setReason( request.getReason());

        return mapToResponse(
                expectedPriceRepository.save(expectedPrice));
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ExpectedPriceResponseDto getExpectedPriceById( Long id) {

        return mapToResponse(
                findExpectedPrice(id));
    }

    // =========================================================
    // GET BY MARKET
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto> getByMarket( Long marketId) {

        if (!marketRepository.existsById(marketId)) {

            throw new ResourceNotFoundException(
                    "Market not found with id: " + marketId);
        }

        return expectedPriceRepository
                .findByMarketIdOrderByExpectedDateDesc( marketId )
                .stream()
                .filter(ExpectedPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET BY MARKET + DATE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ExpectedPriceResponseDto getByMarketAndDate( Long marketId, LocalDate expectedDate) {

        ExpectedPrice expectedPrice =
                expectedPriceRepository
                        .findByMarketIdAndExpectedDate( marketId, expectedDate )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Expected price not found for market and date"
                                ));

        if (!Boolean.TRUE.equals(
                expectedPrice.getActive())) {

            throw new ResourceNotFoundException(
                    "Expected price is not active");
        }

        return mapToResponse(expectedPrice);
    }

    // =========================================================
    // GET ACTIVE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto>
            getActiveExpectedPrices() {

        return expectedPriceRepository
                .findByActiveTrueOrderByExpectedDateDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // DATE RANGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto> getByDateRange( LocalDate startDate, LocalDate endDate) {

        validateDateRange(startDate, endDate);

        return expectedPriceRepository
                .findByExpectedDateBetweenOrderByExpectedDateDesc( startDate, endDate )
                .stream()
                .filter(ExpectedPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // MARKET + DATE RANGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ExpectedPriceResponseDto>
            getMarketDateRange( Long marketId, LocalDate startDate, LocalDate endDate) {

        validateDateRange(startDate, endDate);

        if (!marketRepository.existsById(marketId)) {

            throw new ResourceNotFoundException(
                    "Market not found with id: " + marketId);
        }

        return expectedPriceRepository
                .findByMarketIdAndExpectedDateBetweenOrderByExpectedDateDesc( marketId, startDate, endDate )
                .stream()
                .filter(ExpectedPrice::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // DELETE - SOFT DELETE
    // =========================================================

    @Override
    public void deleteExpectedPrice(Long id) {

        ExpectedPrice expectedPrice =
                findExpectedPrice(id);

        expectedPrice.setActive(false);

        expectedPriceRepository.save(expectedPrice);
    }

    // =========================================================
    // COUNT ACTIVE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countActiveExpectedPrices() {

        return expectedPriceRepository
                .countByActiveTrue();
    }

    // =========================================================
    // FIND EXPECTED PRICE
    // =========================================================

    private ExpectedPrice findExpectedPrice( Long id) {

        return expectedPriceRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expected price not found with id: "
                                        + id
                        ));
    }

    // =========================================================
    // FIND MARKET
    // =========================================================

    private Market findMarket(Long id) {

        return marketRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Market not found with id: "
                                        + id
                        ));
    }

    // =========================================================
    // VALIDATE DATE RANGE
    // =========================================================

    private void validateDateRange( LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {

            throw new BadRequestException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {

            throw new BadRequestException(
                    "Start date cannot be after end date");
        }
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private ExpectedPriceResponseDto mapToResponse( ExpectedPrice expectedPrice) {

        Market market =
                expectedPrice.getMarket();

        City city =
                market.getCity();

        return ExpectedPriceResponseDto.builder()
                .id(expectedPrice.getId())

                .marketId(market.getId())
                .marketName(market.getName())

                .cityName( city != null ? city.getName()
                                : null
                )

                .expectedPrice( expectedPrice.getExpectedPrice()
                )

                .expectedDate( expectedPrice.getExpectedDate()
                )

                .reason( expectedPrice.getReason()
                )

                .active( expectedPrice.getActive()
                )

                .createdAt( expectedPrice.getCreatedAt()
                )

                .updatedAt( expectedPrice.getUpdatedAt()
                )

                .build();
    }
}