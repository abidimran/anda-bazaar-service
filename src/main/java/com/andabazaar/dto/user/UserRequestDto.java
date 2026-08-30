package com.andabazaar.dto.user;

import com.andabazaar.enums.RoleType;

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
public class UserRequestDto {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @ValidMobileNumber
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be 6 to 100 characters")
    private String password;

    private RoleType role;

    private String preferredLanguage;

    private String preferredCity;

    private Boolean notificationEnabled;
}
