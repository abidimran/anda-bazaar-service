package com.andabazaar.dto.user;

import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private RoleType role;

    private UserStatus status;

    private String profileImage;

    private String preferredLanguage;

    private String preferredCity;

    private Boolean notificationEnabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
