package com.andabazaar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "subscriptionPlan.id", target = "planId")
    @Mapping(source = "subscriptionPlan.name", target = "planName")
    PaymentResponseDto toDto(Payment payment);
}
