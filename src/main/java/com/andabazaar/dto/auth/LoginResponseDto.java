package com.andabazaar.dto.auth;

import com.andabazaar.enums.RoleType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;

    private String tokenType;

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private RoleType role;
}
