package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;

public interface PriceReportService {

    PriceReportResponseDto createReport( PriceReportRequestDto request);

    PriceReportResponseDto getReportById( Long id);

    List<PriceReportResponseDto> getAllReports();

    List<PriceReportResponseDto> getUserReports( Long userId);

    List<PriceReportResponseDto> getMarketReports( Long marketId);

    List<PriceReportResponseDto> getReportsByStatus( String status);

    List<PriceReportResponseDto> getPendingReports();

    PriceReportResponseDto reviewReport( Long id, String status, String adminRemarks);

    void deleteReport( Long id);

    long countByStatus( String status);
}