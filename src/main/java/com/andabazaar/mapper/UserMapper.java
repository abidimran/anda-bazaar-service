package com.andabazaar.mapper;

import com.andabazaar.dto.user.UserProfileDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.repository.entity.User;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    UserProfileDto toProfileDto(User user);
}
