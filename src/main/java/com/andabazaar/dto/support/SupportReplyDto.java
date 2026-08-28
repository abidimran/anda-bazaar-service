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
public class SupportReplyDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Message is required")
    @Size(
        max = 3000,
        message = "Message cannot exceed 3000 characters")
    private String message;

    private Boolean adminReply;
}