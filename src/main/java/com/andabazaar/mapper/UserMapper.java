package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.entity.User;

@Component
public class UserMapper {

    public UserResponseDto toDto(User user) {

        if (user == null) {
            return null;
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .preferredLanguage(user.getPreferredLanguage())
                .preferredCity(user.getPreferredCity())
                .notificationEnabled(
                        user.getNotificationEnabled()
                )
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}