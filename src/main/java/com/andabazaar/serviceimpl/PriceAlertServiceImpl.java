package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.alert.PriceAlertRequestDto;
import com.andabazaar.dto.alert.PriceAlertResponseDto;
import com.andabazaar.entity.Market;
import com.andabazaar.entity.PriceAlert;
import com.andabazaar.entity.User;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.PriceAlertRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.PriceAlertService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceAlertServiceImpl
        implements PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;

    private final UserRepository userRepository;

    private final MarketRepository marketRepository;


    // =========================================================
    // CREATE ALERT
    // =========================================================

    @Override
    public PriceAlertResponseDto createAlert(
            PriceAlertRequestDto request) {

        // -----------------------------------------------------
        // VALIDATE REQUEST
        // -----------------------------------------------------

        validateRequest(request);


        // -----------------------------------------------------
        // FIND USER
        // -----------------------------------------------------

        User user = userRepository.findById(
                request.getUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "User not found with id: "
                                + request.getUserId()
                )
        );


        // -----------------------------------------------------
        // FIND MARKET
        // -----------------------------------------------------

        Market market = marketRepository.findById(
                request.getMarketId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Market not found with id: "
                                + request.getMarketId()
                )
        );


        // -----------------------------------------------------
        // CREATE ALERT
        //
        // IMPORTANT:
        // Multiple alerts for same user + same market
        // are now allowed.
        // -----------------------------------------------------

        PriceAlert alert = PriceAlert.builder()

                .user(user)

                .market(market)

                .targetPrice(
                        request.getTargetPrice()
                )

                .condition(
                        request.getCondition()
                )

                .active(
                        request.getActive() != null
                                ? request.getActive()
                                : true
                )

                .build();


        // -----------------------------------------------------
        // SAVE ALERT
        // -----------------------------------------------------

        PriceAlert savedAlert =
                priceAlertRepository.save(alert);


        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(savedAlert);
    }


    // =========================================================
    // GET ALERT BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PriceAlertResponseDto getAlertById(
            Long id) {

        PriceAlert alert = findAlert(id);

        return mapToResponse(alert);
    }


    // =========================================================
    // GET USER ALERTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PriceAlertResponseDto> getUserAlerts(
            Long userId) {

        // -----------------------------------------------------
        // CHECK USER
        // -----------------------------------------------------

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with id: "
                            + userId
            );
        }


        // -----------------------------------------------------
        // GET ALERTS
        // -----------------------------------------------------

        return priceAlertRepository
                .findByUserIdOrderByCreatedAtDesc(userId)

                .stream()

                .map(this::mapToResponse)

                .toList();
    }


    // =========================================================
    // UPDATE ALERT
    // =========================================================

    @Override
    public PriceAlertResponseDto updateAlert(
            Long id,
            PriceAlertRequestDto request) {

        // -----------------------------------------------------
        // VALIDATE REQUEST
        // -----------------------------------------------------

        validateRequest(request);


        // -----------------------------------------------------
        // FIND EXISTING ALERT
        // -----------------------------------------------------

        PriceAlert alert = findAlert(id);


        // -----------------------------------------------------
        // FIND USER
        // -----------------------------------------------------

        User user = userRepository.findById(
                request.getUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "User not found with id: "
                                + request.getUserId()
                )
        );


        // -----------------------------------------------------
        // FIND MARKET
        // -----------------------------------------------------

        Market market = marketRepository.findById(
                request.getMarketId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Market not found with id: "
                                + request.getMarketId()
                )
        );


        // -----------------------------------------------------
        // ACTIVE STATUS
        // -----------------------------------------------------

        boolean active =
                request.getActive() != null
                        ? request.getActive()
                        : Boolean.TRUE.equals(
                                alert.getActive()
                        );


        // -----------------------------------------------------
        // UPDATE ALERT
        // -----------------------------------------------------

        alert.setUser(user);

        alert.setMarket(market);

        alert.setTargetPrice(
                request.getTargetPrice()
        );

        alert.setCondition(
                request.getCondition()
        );

        alert.setActive(active);


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        PriceAlert updatedAlert =
                priceAlertRepository.save(alert);


        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedAlert);
    }


    // =========================================================
    // DELETE ALERT
    // =========================================================

    @Override
    public void deleteAlert(
            Long id) {

        // -----------------------------------------------------
        // FIND ALERT
        // -----------------------------------------------------

        PriceAlert alert = findAlert(id);


        // -----------------------------------------------------
        // DELETE
        // -----------------------------------------------------

        priceAlertRepository.delete(alert);
    }


    // =========================================================
    // TOGGLE ALERT
    // =========================================================

    @Override
    public PriceAlertResponseDto toggleAlert(
            Long id) {

        // -----------------------------------------------------
        // FIND ALERT
        // -----------------------------------------------------

        PriceAlert alert = findAlert(id);


        // -----------------------------------------------------
        // TOGGLE STATUS
        // -----------------------------------------------------

        boolean newStatus =
                !Boolean.TRUE.equals(
                        alert.getActive()
                );


        alert.setActive(newStatus);


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        PriceAlert updatedAlert =
                priceAlertRepository.save(alert);


        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedAlert);
    }


    // =========================================================
    // FIND ALERT
    // =========================================================

    private PriceAlert findAlert(
            Long id) {

        return priceAlertRepository
                .findById(id)

                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Price alert not found with id: "
                                        + id
                        )
                );
    }


    // =========================================================
    // VALIDATE REQUEST
    // =========================================================

    private void validateRequest(
            PriceAlertRequestDto request) {

        // -----------------------------------------------------
        // REQUEST NULL
        // -----------------------------------------------------

        if (request == null) {

            throw new BadRequestException(
                    "Price alert request cannot be null"
            );
        }


        // -----------------------------------------------------
        // USER ID
        // -----------------------------------------------------

        if (request.getUserId() == null) {

            throw new BadRequestException(
                    "User id is required"
            );
        }


        // -----------------------------------------------------
        // MARKET ID
        // -----------------------------------------------------

        if (request.getMarketId() == null) {

            throw new BadRequestException(
                    "Market id is required"
            );
        }


        // -----------------------------------------------------
        // TARGET PRICE
        // -----------------------------------------------------

        if (request.getTargetPrice() == null) {

            throw new BadRequestException(
                    "Target price is required"
            );
        }


        // -----------------------------------------------------
        // NEGATIVE PRICE
        // -----------------------------------------------------

        if (request.getTargetPrice().signum() < 0) {

            throw new BadRequestException(
                    "Target price cannot be negative"
            );
        }


        // -----------------------------------------------------
        // CONDITION
        // -----------------------------------------------------

        if (request.getCondition() == null
                || request.getCondition().isBlank()) {

            throw new BadRequestException(
                    "Alert condition is required"
            );
        }


        // -----------------------------------------------------
        // CONDITION LENGTH
        // -----------------------------------------------------

        if (request.getCondition().length() > 20) {

            throw new BadRequestException(
                    "Alert condition cannot exceed 20 characters"
            );
        }
    }


    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private PriceAlertResponseDto mapToResponse(
            PriceAlert alert) {

        return PriceAlertResponseDto.builder()

                .id(
                        alert.getId()
                )

                .userId(
                        alert.getUser() != null
                                ? alert.getUser().getId()
                                : null
                )

                .marketId(
                        alert.getMarket() != null
                                ? alert.getMarket().getId()
                                : null
                )

                .marketName(
                        alert.getMarket() != null
                                ? alert.getMarket().getName()
                                : null
                )

                .targetPrice(
                        alert.getTargetPrice()
                )

                .condition(
                        alert.getCondition()
                )

                .active(
                        alert.getActive()
                )

                .createdAt(
                        alert.getCreatedAt()
                )

                .updatedAt(
                        alert.getUpdatedAt()
                )

                .build();
    }
}