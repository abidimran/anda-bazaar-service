package com.andabazaar.dto.favorite;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteMarketRequestDto {

    private Long userId;

    private Long marketId;
}