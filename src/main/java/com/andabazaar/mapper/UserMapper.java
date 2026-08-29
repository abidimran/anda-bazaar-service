package com.andabazaar.mapper;

import org.mapstruct.Mapper;

import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.dto.user.UserProfileDto;
import com.andabazaar.repository.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);

    UserProfileDto toProfileDto(User user);
}
