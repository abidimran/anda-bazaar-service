package com.andabazaar.service;

import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;

import java.util.List;

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
