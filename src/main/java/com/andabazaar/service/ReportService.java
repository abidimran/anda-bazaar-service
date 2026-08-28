package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.dto.report.PriceReportReviewRequestDto;

public interface ReportService {

    PriceReportResponseDto createReport( PriceReportRequestDto request);

    PriceReportResponseDto getReportById( Long id);

    List<PriceReportResponseDto> getAllReports();

    List<PriceReportResponseDto> getUserReports( Long userId);

    List<PriceReportResponseDto> getMarketReports( Long marketId);

    List<PriceReportResponseDto> getReportsByStatus( String status);

    List<PriceReportResponseDto> getReviewedReports( Boolean reviewed);

    PriceReportResponseDto reviewReport( Long id, PriceReportReviewRequestDto request);

    void deleteReport( Long id);

    long countPendingReports();
}