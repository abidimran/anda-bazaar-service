
package com.andabazaar.dto.payment;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    @NotNull(message = "Plan ID is required")
    private Long planId;
}

