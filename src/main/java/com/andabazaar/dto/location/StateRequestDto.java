package com.andabazaar.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateRequestDto {

    @NotBlank(message = "State name is required")
    @Size(max = 100)
    private String name;
}