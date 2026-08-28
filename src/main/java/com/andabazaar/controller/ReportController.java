package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.dto.report.PriceReportReviewRequestDto;
import com.andabazaar.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<PriceReportResponseDto> createReport(@Valid @RequestBody PriceReportRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(request));
    }

    @GetMapping
    public ResponseEntity<List<PriceReportResponseDto>> getAllReports() {

 return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceReportResponseDto> getReportById(@PathVariable Long id) {

 return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PriceReportResponseDto>> getUserReports(@PathVariable Long userId) {

 return ResponseEntity.ok(reportService.getUserReports(userId));
    }

    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<PriceReportResponseDto>> getMarketReports(@PathVariable Long marketId) {

 return ResponseEntity.ok(reportService.getMarketReports(marketId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PriceReportResponseDto>> getByStatus(@PathVariable String status) {

 return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PriceReportResponseDto>> getPendingReports() {

 return ResponseEntity.ok(reportService.getReportsByStatus("PENDING"));
    }

    @GetMapping("/reviewed/{reviewed}")
    public ResponseEntity<List<PriceReportResponseDto>> getReviewedReports(@PathVariable Boolean reviewed) {

 return ResponseEntity.ok(reportService.getReviewedReports(reviewed));
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<PriceReportResponseDto> reviewReport(@PathVariable Long id,
            @Valid @RequestBody PriceReportReviewRequestDto request) {

 return ResponseEntity.ok(reportService.reviewReport(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {

        reportService.deleteReport(id);

 return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/pending")
    public ResponseEntity<Long> countPendingReports() {

 return ResponseEntity.ok(reportService.countPendingReports());
    }
}