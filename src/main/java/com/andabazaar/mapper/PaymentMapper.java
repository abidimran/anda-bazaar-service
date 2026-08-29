package com.andabazaar.mapper;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.repository.entity.Payment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "user.id", target = "userId")
    PaymentResponseDto toDto(Payment payment);
}
