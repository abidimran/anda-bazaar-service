package com.andabazaar.dto.support;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketRequestDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject cannot exceed 200 characters")
    private String subject;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 20, message = "Priority cannot exceed 20 characters")
    private String priority;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
}