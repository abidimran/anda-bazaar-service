package com.andabazaar.dto.user;

import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private RoleType role;

    private UserStatus status;

    private String profileImage;

    private String preferredLanguage;

    private String preferredCity;

    private Boolean notificationEnabled;
}
