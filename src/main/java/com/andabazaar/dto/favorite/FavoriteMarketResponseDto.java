package com.andabazaar.dto.favorite;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteMarketResponseDto {

    private Long id;

    private Long userId;

    private Long marketId;

    private String marketName;

    private String cityName;

    private LocalDateTime createdAt;
}