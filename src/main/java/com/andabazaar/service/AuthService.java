package com.andabazaar.service;

import com.andabazaar.dto.auth.LoginRequestDto;
import com.andabazaar.dto.auth.LoginResponseDto;
import com.andabazaar.dto.auth.RegisterRequestDto;
import com.andabazaar.dto.user.UserResponseDto;

public interface AuthService {
    UserResponseDto register(RegisterRequestDto request);

    LoginResponseDto login(LoginRequestDto request);

    UserResponseDto getCurrentUser(String email);
}
