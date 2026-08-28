package com.andabazaar.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
}