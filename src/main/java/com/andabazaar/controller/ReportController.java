package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
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
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getAllReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(reportService.getAllReports(), page, size));
    }

    @Operation(summary = "Get Report By Id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceReportResponseDto> getReportById(@PathVariable Long id) {

 return ResponseEntity.ok(reportService.getReportById(id));
    }

    @Operation(summary = "Get User Reports")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getUserReports(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(reportService.getUserReports(userId), page, size));
    }

    @Operation(summary = "Get Market Reports")
    @GetMapping("/market/{marketId}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getMarketReports(@PathVariable Long marketId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(reportService.getMarketReports(marketId), page, size));
    }

    @Operation(summary = "Get By Status")
    @GetMapping("/status/{status}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getByStatus(@PathVariable String status,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(reportService.getReportsByStatus(status), page, size));
    }

    @Operation(summary = "Get Pending Reports")
    @GetMapping("/pending")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getPendingReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(reportService.getReportsByStatus("PENDING"), page, size));
    }

    @Operation(summary = "Get Reviewed Reports")
    @GetMapping("/reviewed/{reviewed}")
    public ResponseEntity<PagedResponse<PriceReportResponseDto>>
            getReviewedReports(@PathVariable Boolean reviewed,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(reportService.getReviewedReports(reviewed), page, size));
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
