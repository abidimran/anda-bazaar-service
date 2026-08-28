package com.andabazaar.dto.appsetting;

import jakarta.validation.constraints.NotBlank;
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
public class AppSettingRequestDto {

    @NotBlank(message = "Setting key is required")
    @Size(max = 100, message = "Setting key cannot exceed 100 characters")
    private String settingKey;

    @Size(max = 1000, message = "Setting value cannot exceed 1000 characters")
    private String settingValue;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean active;
}