package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.dto.user.UserProfileDto;

public interface UserService {

    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserRequestDto request);

    void deleteUser(Long id);

    UserProfileDto getProfile(Long id);

    UserResponseDto changeUserStatus(Long id, String status);
}