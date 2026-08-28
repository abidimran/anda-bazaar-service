package com.andabazaar.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.dto.report.PriceReportReviewRequestDto;
import com.andabazaar.entity.Market;
import com.andabazaar.entity.PriceReport;
import com.andabazaar.entity.User;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.PriceReportRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final PriceReportRepository priceReportRepository;

    private final UserRepository userRepository;

    private final MarketRepository marketRepository;

    @Override
    public PriceReportResponseDto createReport( PriceReportRequestDto request) {

        if (request == null) {
            throw new IllegalArgumentException("Report request cannot be null");
        }

        // -----------------------------------------------------
        // FIND USER
        // -----------------------------------------------------

        User user = userRepository.findById( request.getUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: "
                                + request.getUserId()
                ));

        // -----------------------------------------------------
        // FIND MARKET
        // -----------------------------------------------------

        Market market = marketRepository.findById( request.getMarketId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Market not found with id: "
                                + request.getMarketId()
                ));

        // -----------------------------------------------------
        // CREATE ENTITY
        // -----------------------------------------------------

        PriceReport report = PriceReport.builder()
                .user(user)
                .market(market)
                .reportedPrice( request.getReportedPrice()
                )
                .reason( request.getReason().trim()
                )
                .description( request.getDescription() == null ? null : request.getDescription().trim()
                )
                .status("PENDING")
                .reviewed(false)
                .adminRemarks(null)
                .build();

        PriceReport saved =
                priceReportRepository.save(report);

        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PriceReportResponseDto getReportById( Long id) {

        PriceReport report =
                priceReportRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Price report not found with id: "
                                                + id
                                ));

        return convertToResponse(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto> getAllReports() {

        return priceReportRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto> getUserReports( Long userId) {

        return priceReportRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto> getMarketReports( Long marketId) {

        return priceReportRepository
                .findByMarketIdOrderByCreatedAtDesc(marketId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto> getReportsByStatus( String status) {

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Report status is required");
        }

        return priceReportRepository
                .findByStatusOrderByCreatedAtDesc( status.trim().toUpperCase()
                )
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto> getReviewedReports( Boolean reviewed) {

        return priceReportRepository
                .findByReviewedOrderByCreatedAtDesc( reviewed )
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PriceReportResponseDto reviewReport( Long id, PriceReportReviewRequestDto request) {

        if (request == null) {
            throw new IllegalArgumentException("Review request cannot be null");
        }

        PriceReport report =
                priceReportRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Price report not found with id: "
                                                + id
                                ));

        if (request.getStatus() == null
                || request.getStatus().trim().isEmpty()) {

            throw new IllegalArgumentException("Status is required");
        }

        String status =
                request.getStatus()
                        .trim()
                        .toUpperCase();

        // -----------------------------------------------------
        // ONLY CONFIRMED OR REJECTED
        // -----------------------------------------------------

        if (!status.equals("CONFIRMED")
                && !status.equals("REJECTED")) {

            throw new IllegalArgumentException("Status must be CONFIRMED or REJECTED");
        }

        // -----------------------------------------------------
        // UPDATE
        // -----------------------------------------------------

        report.setStatus(status);

        report.setReviewed(true);

        report.setAdminRemarks( request.getAdminRemarks() == null ? null : request.getAdminRemarks().trim());

        PriceReport updated =
                priceReportRepository.save(report);

        return convertToResponse(updated);
    }

    @Override
    public void deleteReport(Long id) {

        PriceReport report =
                priceReportRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Price report not found with id: "
                                                + id
                                ));

        priceReportRepository.delete(report);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendingReports() {

        return priceReportRepository.countByStatus("PENDING");
    }

    private PriceReportResponseDto convertToResponse( PriceReport report) {

        // -----------------------------------------------------
        // USER NAME
        // -----------------------------------------------------

        String userName = null;

        if (report.getUser() != null) {

            userName = buildUserName( report.getUser());
        }

        // -----------------------------------------------------
        // MARKET NAME + CITY NAME
        // -----------------------------------------------------

        String marketName = null;

        String cityName = null;

        if (report.getMarket() != null) {

            // Market name
            marketName =
                    report.getMarket().getName();

            // City name
            if (report.getMarket().getCity() != null) {

                cityName =
                        report.getMarket()
                                .getCity()
                                .getName();
            }
        }

        // -----------------------------------------------------
        // RESPONSE DTO
        // -----------------------------------------------------

        return PriceReportResponseDto.builder()

                .id(report.getId())

                .userId( report.getUser() != null ? report.getUser().getId()
                                : null
                )

                .userName(userName)

                .marketId( report.getMarket() != null ? report.getMarket().getId()
                                : null
                )

                .marketName(marketName)

                .cityName(cityName)

                .reportedPrice( report.getReportedPrice()
                )

                .reason( report.getReason()
                )

                .description( report.getDescription()
                )

                .status( report.getStatus()
                )

                .reviewed( report.getReviewed()
                )

                .adminRemarks( report.getAdminRemarks()
                )

                .createdAt( report.getCreatedAt()
                )

                .updatedAt( report.getUpdatedAt()
                )

                .build();
    }

    private String buildUserName(User user) {

        String firstName =
                user.getFirstName() == null
                        ? ""
                        : user.getFirstName().trim();

        String lastName =
                user.getLastName() == null
                        ? ""
                        : user.getLastName().trim();

        return (firstName + " " + lastName)
                .trim();
    }
}