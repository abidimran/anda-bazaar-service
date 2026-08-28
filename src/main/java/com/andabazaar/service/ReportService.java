package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.dto.report.PriceReportReviewRequestDto;

public interface ReportService {

    // =========================================================
    // CREATE
    // =========================================================

    PriceReportResponseDto createReport( PriceReportRequestDto request);

    // =========================================================
    // GET BY ID
    // =========================================================

    PriceReportResponseDto getReportById( Long id);

    // =========================================================
    // GET ALL
    // =========================================================

    List<PriceReportResponseDto> getAllReports();

    // =========================================================
    // GET USER REPORTS
    // =========================================================

    List<PriceReportResponseDto> getUserReports( Long userId);

    // =========================================================
    // GET MARKET REPORTS
    // =========================================================

    List<PriceReportResponseDto> getMarketReports( Long marketId);

    // =========================================================
    // GET BY STATUS
    // =========================================================

    List<PriceReportResponseDto> getReportsByStatus( String status);

    // =========================================================
    // GET REVIEWED
    // =========================================================

    List<PriceReportResponseDto> getReviewedReports( Boolean reviewed);

    // =========================================================
    // REVIEW
    // =========================================================

    PriceReportResponseDto reviewReport( Long id, PriceReportReviewRequestDto request);

    // =========================================================
    // DELETE
    // =========================================================

    void deleteReport( Long id);

    // =========================================================
    // COUNT PENDING
    // =========================================================

    long countPendingReports();
}