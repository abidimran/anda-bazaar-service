package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.report.PriceReportRequestDto;
import com.andabazaar.dto.report.PriceReportResponseDto;
import com.andabazaar.service.PriceReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Price Reports", description = "User-submitted price reports")
@RestController
@RequestMapping("/api/price-reports")
@RequiredArgsConstructor
public class PriceReportController {

    private final PriceReportService priceReportService;

    // Create price report
    @Operation(summary = "Create Report")
    @PostMapping
    public ResponseEntity<PriceReportResponseDto> createReport(@Valid @RequestBody PriceReportRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(priceReportService.createReport(request));
    }

    // Get report by ID
    @Operation(summary = "Get Report By Id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceReportResponseDto> getReportById(@PathVariable Long id) {

 return ResponseEntity.ok(priceReportService.getReportById(id));
    }

    // Get all reports
    @Operation(summary = "Get All Reports")
    @GetMapping
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getAllReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceReportService.getAllReports(), page, size));
    }

    // Get reports by user
    @Operation(summary = "Get User Reports")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getUserReports(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceReportService.getUserReports(userId), page, size));
    }

    // Get reports by market
    @Operation(summary = "Get Market Reports")
    @GetMapping("/market/{marketId}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getMarketReports(@PathVariable Long marketId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceReportService.getMarketReports(marketId), page, size));
    }

    // Get reports by status
    @Operation(summary = "Get Reports By Status")
    @GetMapping("/status/{status}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getReportsByStatus(@PathVariable String status,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceReportService.getReportsByStatus(status), page, size));
    }

    // Get pending reports
    @Operation(summary = "Get Pending Reports")
    @GetMapping("/pending")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getPendingReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceReportService.getPendingReports(), page, size));
    }

    // Review report
    @Operation(summary = "Review Report")
    @PutMapping("/{id}/review")
    public ResponseEntity<PriceReportResponseDto> reviewReport(@PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String adminRemarks) {

 return ResponseEntity.ok(priceReportService.reviewReport(id, status, adminRemarks));
    }

    // Count reports by status
    @Operation(summary = "Count By Status")
    @GetMapping("/count/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {

 return ResponseEntity.ok(priceReportService.countByStatus(status));
    }

    // Delete report
    @Operation(summary = "Delete Report")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {

        priceReportService.deleteReport(id);

 return ResponseEntity.noContent().build();
    }
}
