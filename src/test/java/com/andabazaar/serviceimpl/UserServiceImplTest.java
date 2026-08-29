package com.andabazaar.serviceimpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andabazaar.dto.user.UserProfileDto;
import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.repository.entity.User;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encodedPassword")
                .role(RoleType.USER)
                .status(UserStatus.ACTIVE)
                .preferredLanguage("en")
                .preferredCity("Bangalore")
                .notificationEnabled(true)
                .build();

        requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("password123")
                .preferredLanguage("en")
                .preferredCity("Bangalore")
                .notificationEnabled(true)
                .build();
    }

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Test
        @DisplayName("should create user successfully")
        void shouldCreateUserSuccessfully() {
            when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(userRepository.existsByPhone("1234567890")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserResponseDto result = userService.createUser(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getRole()).isEqualTo(RoleType.USER);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should create user with null notificationEnabled defaults to true")
        void shouldCreateUserWithNullNotificationEnabled() {
            requestDto.setNotificationEnabled(null);

            when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(userRepository.existsByPhone("1234567890")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserResponseDto result = userService.createUser(requestDto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when email already registered")
        void shouldThrowWhenEmailAlreadyRegistered() {
            when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Email already registered");
        }

        @Test
        @DisplayName("should throw when phone already registered")
        void shouldThrowWhenPhoneAlreadyRegistered() {
            when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(userRepository.existsByPhone("1234567890")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Phone number already registered");
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {

        @Test
        @DisplayName("should return user by id")
        void shouldReturnUserById() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            UserResponseDto result = userService.getUserById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when id is null")
        void shouldThrowWhenIdIsNull() {
            assertThatThrownBy(() -> userService.getUserById(null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("User ID is required");
        }
    }

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {

        @Test
        @DisplayName("should return all users")
        void shouldReturnAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(user));

            List<UserResponseDto> result = userService.getAllUsers();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should return empty list when no users")
        void shouldReturnEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<UserResponseDto> result = userService.getAllUsers();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("should update user successfully")
        void shouldUpdateUserSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserResponseDto result = userService.updateUser(1L, requestDto);

            assertThat(result).isNotNull();
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should update password when provided")
        void shouldUpdatePasswordWhenProvided() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(passwordEncoder.encode("password123")).thenReturn("newEncodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.updateUser(1L, requestDto);

            verify(passwordEncoder).encode("password123");
        }

        @Test
        @DisplayName("should not update password when null")
        void shouldNotUpdatePasswordWhenNull() {
            requestDto.setPassword(null);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.updateUser(1L, requestDto);

            verify(passwordEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("should not update password when blank")
        void shouldNotUpdatePasswordWhenBlank() {
            requestDto.setPassword("   ");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.updateUser(1L, requestDto);

            verify(passwordEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("should throw when email duplicate on update")
        void shouldThrowWhenEmailDuplicate() {
            requestDto.setEmail("other@example.com");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail("other@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.updateUser(1L, requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Email already registered");
        }

        @Test
        @DisplayName("should throw when phone duplicate on update")
        void shouldThrowWhenPhoneDuplicate() {
            requestDto.setPhone("9999999999");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.existsByPhone("9999999999")).thenReturn(true);

            assertThatThrownBy(() -> userService.updateUser(1L, requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Phone number already registered");
        }

        @Test
        @DisplayName("should update notificationEnabled when provided")
        void shouldUpdateNotificationEnabled() {
            requestDto.setNotificationEnabled(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            userService.updateUser(1L, requestDto);

            verify(userRepository).save(argThat(u -> Boolean.FALSE.equals(u.getNotificationEnabled())));
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {

        @Test
        @DisplayName("should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            userService.deleteUser(1L);

            verify(userRepository).delete(user);
        }
    }

    @Nested
    @DisplayName("getProfile")
    class GetProfile {

        @Test
        @DisplayName("should return user profile")
        void shouldReturnProfile() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            UserProfileDto result = userService.getProfile(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getEmail()).isEqualTo("john@example.com");
            assertThat(result.getRole()).isEqualTo(RoleType.USER);
        }
    }

    @Nested
    @DisplayName("changeUserStatus")
    class ChangeUserStatus {

        @Test
        @DisplayName("should change user status successfully")
        void shouldChangeStatusSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserResponseDto result = userService.changeUserStatus(1L, "INACTIVE");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when status is invalid")
        void shouldThrowWhenInvalidStatus() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> userService.changeUserStatus(1L, "INVALID"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Invalid user status");
        }

        @Test
        @DisplayName("should throw when status is null")
        void shouldThrowWhenStatusIsNull() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> userService.changeUserStatus(1L, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("User status is required");
        }

        @Test
        @DisplayName("should throw when status is blank")
        void shouldThrowWhenStatusIsBlank() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> userService.changeUserStatus(1L, "  "))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("User status is required");
        }
    }
}
