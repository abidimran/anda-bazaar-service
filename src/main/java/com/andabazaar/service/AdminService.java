package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;

public interface AdminService {

    // Create Admin
    UserResponseDto createAdmin(UserRequestDto request);

    // Get all users
    List<UserResponseDto> getAllUsers();

    // Get user by ID
    UserResponseDto getUser(Long id);

    // Change user status
    UserResponseDto changeUserStatus(Long id, String status);

    // Delete user
    void deleteUser(Long id);
}