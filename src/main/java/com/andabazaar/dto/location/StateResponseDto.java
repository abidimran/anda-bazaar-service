package com.andabazaar.dto.location;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateResponseDto {

    private Long id;

    private String name;

    private Boolean active;
}