package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.dto.report.PriceReportReviewRequestDto;
import com.andabazaar.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Reports", description = "Admin report management and review")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Create Report")
    @PostMapping
    public ResponseEntity<PriceReportResponseDto> createReport(@Valid @RequestBody PriceReportRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(request));
    }

    @Operation(summary = "Get All Reports")
    @GetMapping
    public ResponseEntity<List<PriceReportResponseDto>> getAllReports() {

 return ResponseEntity.ok(reportService.getAllReports());
    }

    @Operation(summary = "Get Report By Id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceReportResponseDto> getReportById(@PathVariable Long id) {

 return ResponseEntity.ok(reportService.getReportById(id));
    }

    @Operation(summary = "Get User Reports")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PriceReportResponseDto>> getUserReports(@PathVariable Long userId) {

 return ResponseEntity.ok(reportService.getUserReports(userId));
    }

    @Operation(summary = "Get Market Reports")
    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<PriceReportResponseDto>> getMarketReports(@PathVariable Long marketId) {

 return ResponseEntity.ok(reportService.getMarketReports(marketId));
    }

    @Operation(summary = "Get By Status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PriceReportResponseDto>> getByStatus(@PathVariable String status) {

 return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    @Operation(summary = "Get Pending Reports")
    @GetMapping("/pending")
    public ResponseEntity<List<PriceReportResponseDto>> getPendingReports() {

 return ResponseEntity.ok(reportService.getReportsByStatus("PENDING"));
    }

    @Operation(summary = "Get Reviewed Reports")
    @GetMapping("/reviewed/{reviewed}")
    public ResponseEntity<List<PriceReportResponseDto>> getReviewedReports(@PathVariable Boolean reviewed) {

 return ResponseEntity.ok(reportService.getReviewedReports(reviewed));
    }

    @Operation(summary = "Review Report")
    @PutMapping("/{id}/review")
    public ResponseEntity<PriceReportResponseDto> reviewReport(@PathVariable Long id,
            @Valid @RequestBody PriceReportReviewRequestDto request) {

 return ResponseEntity.ok(reportService.reviewReport(id, request));
    }

    @Operation(summary = "Delete Report")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {

        reportService.deleteReport(id);

 return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Count Pending Reports")
    @GetMapping("/count/pending")
    public ResponseEntity<Long> countPendingReports() {

 return ResponseEntity.ok(reportService.countPendingReports());
    }
}