package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.entity.EggPrice;

@Component
public class EggPriceMapper {

    public EggPriceResponseDto toDto(EggPrice eggPrice) {

        if (eggPrice == null) {
            return null;
        }

        return EggPriceResponseDto.builder()
                .id(eggPrice.getId())
                .priceDate(eggPrice.getPriceDate())
                .pricePerEgg(eggPrice.getPricePerEgg())
                .marketId(
                        eggPrice.getMarket() != null
                                ? eggPrice.getMarket().getId()
                                : null
                )
                .marketName(
                        eggPrice.getMarket() != null
                                ? eggPrice.getMarket().getName()
                                : null
                )
                .build();
    }
}