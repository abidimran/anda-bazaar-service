package com.andabazaar.mapper;

import org.mapstruct.Mapper;

import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);
}
