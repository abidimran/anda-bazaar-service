package com.andabazaar.dto.auth;

import com.andabazaar.validation.ValidMobileNumber;

import jakarta.validation.constraints.Email;
import com.andabazaar.validation.ValidMobileNumber;

import jakarta.validation.constraints.NotBlank;
import com.andabazaar.validation.ValidMobileNumber;

import com.andabazaar.validation.ValidMobileNumber;

import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @ValidMobileNumber
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @Size(
        min = 6,
        max = 100,
        message = "Password must be 6 to 100 characters")
    private String password;

    private String preferredLanguage;

    private String preferredCity;
}
