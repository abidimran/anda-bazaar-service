package com.andabazaar.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.andabazaar.repository.entity.User;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").phone("1234567890")
                .password("encodedPassword").role(RoleType.USER)
                .status(UserStatus.ACTIVE).build();
    }

    @Nested
    @DisplayName("loadUserByUsername")
    class LoadUserByUsername {

        @Test
        @DisplayName("should return UserDetails for existing user")
        void shouldReturnUserDetails() {
            when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

            UserDetails result = userDetailsService.loadUserByUsername("john@example.com");

            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("john@example.com");
            assertThat(result).isInstanceOf(CustomUserDetails.class);
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown@example.com"))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining("User not found");
        }
    }
}
