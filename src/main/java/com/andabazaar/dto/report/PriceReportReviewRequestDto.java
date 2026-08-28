package com.andabazaar.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceReportReviewRequestDto {

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "CONFIRMED|REJECTED",
        message = "Status must be CONFIRMED or REJECTED")
    private String status;

    @Size(
        max = 1000,
        message = "Admin remarks cannot exceed 1000 characters")
    private String adminRemarks;
}