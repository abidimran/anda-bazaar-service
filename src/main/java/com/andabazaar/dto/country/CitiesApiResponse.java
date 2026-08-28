package com.andabazaar.dto.country;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitiesApiResponse {

    private Boolean error;
    private String msg;
    private List<String> data;
}
