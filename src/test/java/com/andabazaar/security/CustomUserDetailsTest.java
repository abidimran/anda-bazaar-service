package com.andabazaar.security;

import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.repository.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomUserDetails Tests")
class CustomUserDetailsTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .mobileNumber("1234567890")
                .password("encodedPassword")
                .role(RoleType.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Nested
    @DisplayName("getAuthorities")
    class GetAuthorities {
        @Test
        @DisplayName("should return ROLE_USER authority")
        void shouldReturnUserAuthority() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.getAuthorities())
                    .hasSize(1)
                    .extracting(auth -> auth.getAuthority())
                    .contains("ROLE_USER");
        }

        @Test
        @DisplayName("should return ROLE_ADMIN authority")
        void shouldReturnAdminAuthority() {
            user.setRole(RoleType.ADMIN);
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.getAuthorities())
                    .extracting(auth -> auth.getAuthority())
                    .contains("ROLE_ADMIN");
        }
    }

    @Nested
    @DisplayName("isEnabled")
    class IsEnabled {
        @Test
        @DisplayName("should return true for active user")
        void shouldReturnTrueForActive() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isEnabled()).isTrue();
        }

        @Test
        @DisplayName("should return false for inactive user")
        void shouldReturnFalseForInactive() {
            user.setStatus(UserStatus.INACTIVE);
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isEnabled()).isFalse();
        }

        @Test
        @DisplayName("should return false for blocked user")
        void shouldReturnFalseForBlocked() {
            user.setStatus(UserStatus.BLOCKED);
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isEnabled()).isFalse();
        }
    }

    @Nested
    @DisplayName("isAccountNonLocked")
    class IsAccountNonLocked {
        @Test
        @DisplayName("should return true for active user")
        void shouldReturnTrueForActive() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isAccountNonLocked()).isTrue();
        }

        @Test
        @DisplayName("should return false for blocked user")
        void shouldReturnFalseForBlocked() {
            user.setStatus(UserStatus.BLOCKED);
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isAccountNonLocked()).isFalse();
        }
    }

    @Nested
    @DisplayName("other methods")
    class OtherMethods {
        @Test
        @DisplayName("should return password")
        void shouldReturnPassword() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.getPassword()).isEqualTo("encodedPassword");
        }

        @Test
        @DisplayName("should return email as username")
        void shouldReturnEmailAsUsername() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.getUsername()).isEqualTo("john@example.com");
        }

        @Test
        @DisplayName("should return true for account non expired")
        void shouldReturnTrueForAccountNonExpired() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isAccountNonExpired()).isTrue();
        }

        @Test
        @DisplayName("should return true for credentials non expired")
        void shouldReturnTrueForCredentialsNonExpired() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.isCredentialsNonExpired()).isTrue();
        }

        @Test
        @DisplayName("should return user object")
        void shouldReturnUser() {
            CustomUserDetails details = new CustomUserDetails(user);
            assertThat(details.getUser()).isEqualTo(user);
        }
    }
}
