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

    // =========================================================
    // CREATE REPORT
    // =========================================================

    @PostMapping
    public ResponseEntity<PriceReportResponseDto> createReport(@Valid @RequestBody PriceReportRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(request));
    }

    // =========================================================
    // GET ALL REPORTS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<PriceReportResponseDto>> getAllReports() {

 return ResponseEntity.ok(reportService.getAllReports());
    }

    // =========================================================
    // GET REPORT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<PriceReportResponseDto> getReportById(@PathVariable Long id) {

 return ResponseEntity.ok(reportService.getReportById(id));
    }

    // =========================================================
    // GET USER REPORTS
    // =========================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PriceReportResponseDto>> getUserReports(@PathVariable Long userId) {

 return ResponseEntity.ok(reportService.getUserReports(userId));
    }

    // =========================================================
    // GET MARKET REPORTS
    // =========================================================

    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<PriceReportResponseDto>> getMarketReports(@PathVariable Long marketId) {

 return ResponseEntity.ok(reportService.getMarketReports(marketId));
    }

    // =========================================================
    // GET BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PriceReportResponseDto>> getByStatus(@PathVariable String status) {

 return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    // =========================================================
    // GET PENDING REPORTS
    // =========================================================

    @GetMapping("/pending")
    public ResponseEntity<List<PriceReportResponseDto>> getPendingReports() {

 return ResponseEntity.ok(reportService.getReportsByStatus("PENDING"));
    }

    // =========================================================
    // GET REVIEWED REPORTS
    // =========================================================

    @GetMapping("/reviewed/{reviewed}")
    public ResponseEntity<List<PriceReportResponseDto>> getReviewedReports(@PathVariable Boolean reviewed) {

 return ResponseEntity.ok(reportService.getReviewedReports(reviewed));
    }

    // =========================================================
    // REVIEW REPORT
    // =========================================================

    @PutMapping("/{id}/review")
    public ResponseEntity<PriceReportResponseDto> reviewReport(@PathVariable Long id,
            @Valid @RequestBody PriceReportReviewRequestDto request) {

 return ResponseEntity.ok(reportService.reviewReport(id, request));
    }

    // =========================================================
    // DELETE REPORT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {

        reportService.deleteReport(id);

 return ResponseEntity.noContent().build();
    }

    // =========================================================
    // COUNT PENDING
    // =========================================================

    @GetMapping("/count/pending")
    public ResponseEntity<Long> countPendingReports() {

 return ResponseEntity.ok(reportService.countPendingReports());
    }
}