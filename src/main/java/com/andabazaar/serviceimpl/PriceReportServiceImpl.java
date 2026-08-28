package com.andabazaar.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.entity.Market;
import com.andabazaar.entity.PriceReport;
import com.andabazaar.entity.User;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.MarketRepository;
import com.andabazaar.repository.PriceReportRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.PriceReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceReportServiceImpl
        implements PriceReportService {

    private final PriceReportRepository priceReportRepository;
    private final UserRepository userRepository;
    private final MarketRepository marketRepository;

    @Override
    public PriceReportResponseDto createReport( PriceReportRequestDto request) {

        User user = userRepository.findById( request.getUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "User not found with id: "
                                + request.getUserId()
                ));

        Market market = marketRepository.findById( request.getMarketId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Market not found with id: "
                                + request.getMarketId()
                ));

        if (request.getReportedPrice() == null
                || request.getReportedPrice()
                        .signum() < 0) {

            throw new BadRequestException(
                    "Reported price cannot be negative");
        }

        PriceReport report = PriceReport.builder()
                .user(user)
                .market(market)
                .reportedPrice( request.getReportedPrice()
                )
                .reason( request.getReason()
                                .trim()
                )
                .description( request.getDescription()
                )
                .status("PENDING")
                .reviewed(false)
                .build();

        return mapToResponse(
                priceReportRepository.save(report));
    }

    @Override
    @Transactional(readOnly = true)
    public PriceReportResponseDto getReportById( Long id) {

        return mapToResponse(
                findReport(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto>
            getAllReports() {

        return priceReportRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto>
            getUserReports(Long userId) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with id: "
                            + userId);
        }

        return priceReportRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto>
            getMarketReports(Long marketId) {

        if (!marketRepository.existsById(marketId)) {

            throw new ResourceNotFoundException(
                    "Market not found with id: "
                            + marketId);
        }

        return priceReportRepository
                .findByMarketIdOrderByCreatedAtDesc(marketId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto>
            getReportsByStatus(String status) {

        String normalizedStatus =
                normalizeStatus(status);

        return priceReportRepository
                .findByStatusOrderByCreatedAtDesc( normalizedStatus )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriceReportResponseDto>
            getPendingReports() {

        return priceReportRepository
                .findByStatusOrderByCreatedAtDesc( "PENDING" )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PriceReportResponseDto reviewReport( Long id, String status, String adminRemarks) {

        PriceReport report = findReport(id);

        String normalizedStatus =
                normalizeStatus(status);

        if (!normalizedStatus.equals("APPROVED")
                && !normalizedStatus.equals("REJECTED")
                && !normalizedStatus.equals("PENDING")) {

            throw new BadRequestException(
                    "Status must be PENDING, APPROVED or REJECTED");
        }

        report.setStatus(normalizedStatus);
        report.setReviewed( !normalizedStatus.equals("PENDING"));
        report.setAdminRemarks(adminRemarks);

        return mapToResponse(
                priceReportRepository.save(report));
    }

    @Override
    public void deleteReport(Long id) {

        PriceReport report = findReport(id);

        priceReportRepository.delete(report);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {

        return priceReportRepository.countByStatus(
                normalizeStatus(status));
    }

    private PriceReport findReport(Long id) {

        return priceReportRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Price report not found with id: "
                                        + id
                        ));
    }

    private String normalizeStatus(String status) {

        if (status == null
                || status.trim().isEmpty()) {

            throw new BadRequestException(
                    "Status is required");
        }

        return status.trim().toUpperCase();
    }

    private PriceReportResponseDto mapToResponse( PriceReport report) {

        User user = report.getUser();
        Market market = report.getMarket();

        String userName = null;

        if (user != null) {

            String firstName = user.getFirstName();
            String lastName = user.getLastName();

            userName =
                    ((firstName != null) ? firstName : "")
                    + " "
                    + ((lastName != null) ? lastName : "");

            userName = userName.trim();

            if (userName.isEmpty()) {
                userName = null;
            }
        }

        return PriceReportResponseDto.builder()
                .id(report.getId())

                .userId( user != null ? user.getId()
                                : null
                )

                .userName(userName)

                .marketId( market != null ? market.getId()
                                : null
                )

                .marketName( market != null ? market.getName()
                                : null
                )

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
}