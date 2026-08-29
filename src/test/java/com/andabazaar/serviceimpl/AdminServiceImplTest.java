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

import com.andabazaar.dto.user.UserRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.repository.entity.User;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.UserMapper;
import com.andabazaar.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminServiceImpl Tests")
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User admin;
    private UserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        admin = User.builder()
                .id(1L)
                .firstName("Admin")
                .lastName("User")
                .email("admin@example.com")
                .phone("9876543210")
                .password("encodedPassword")
                .role(RoleType.ADMIN)
                .status(UserStatus.ACTIVE)
                .preferredLanguage("en")
                .preferredCity("Mumbai")
                .notificationEnabled(true)
                .build();

        lenient().when(userMapper.toResponseDto(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return UserResponseDto.builder()
                    .id(u.getId()).firstName(u.getFirstName()).lastName(u.getLastName())
                    .email(u.getEmail()).phone(u.getPhone()).role(u.getRole())
                    .status(u.getStatus()).profileImage(u.getProfileImage())
                    .preferredLanguage(u.getPreferredLanguage()).preferredCity(u.getPreferredCity())
                    .notificationEnabled(u.getNotificationEnabled())
                    .createdAt(u.getCreatedAt()).updatedAt(u.getUpdatedAt()).build();
        });

        requestDto = UserRequestDto.builder()
                .firstName("Admin")
                .lastName("User")
                .email("admin@example.com")
                .phone("9876543210")
                .password("admin123")
                .preferredLanguage("en")
                .preferredCity("Mumbai")
                .notificationEnabled(null)
                .build();
    }

    @Nested
    @DisplayName("createAdmin")
    class CreateAdmin {

        @Test
        @DisplayName("should create admin successfully")
        void shouldCreateAdminSuccessfully() {
            when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
            when(userRepository.existsByPhone("9876543210")).thenReturn(false);
            when(passwordEncoder.encode("admin123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(admin);

            UserResponseDto result = adminService.createAdmin(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getRole()).isEqualTo(RoleType.ADMIN);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should create admin with notificationEnabled as true when null")
        void shouldDefaultNotificationEnabled() {
            requestDto.setNotificationEnabled(null);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByPhone(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(admin);

            adminService.createAdmin(requestDto);

            verify(userRepository).save(argThat(u -> Boolean.TRUE.equals(u.getNotificationEnabled())));
        }

        @Test
        @DisplayName("should create admin with notificationEnabled as false when specified")
        void shouldRespectNotificationEnabledFalse() {
            requestDto.setNotificationEnabled(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByPhone(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(admin);

            adminService.createAdmin(requestDto);

            verify(userRepository).save(argThat(u -> Boolean.FALSE.equals(u.getNotificationEnabled())));
        }

        @Test
        @DisplayName("should throw when email already registered")
        void shouldThrowWhenEmailExists() {
            when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);

            assertThatThrownBy(() -> adminService.createAdmin(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Email already registered");
        }

        @Test
        @DisplayName("should throw when phone already registered")
        void shouldThrowWhenPhoneExists() {
            when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
            when(userRepository.existsByPhone("9876543210")).thenReturn(true);

            assertThatThrownBy(() -> adminService.createAdmin(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Phone number already registered");
        }
    }

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {

        @Test
        @DisplayName("should return all users")
        void shouldReturnAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(admin));

            List<UserResponseDto> result = adminService.getAllUsers();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getUser")
    class GetUser {

        @Test
        @DisplayName("should return user by id")
        void shouldReturnUserById() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

            UserResponseDto result = adminService.getUser(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminService.getUser(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("changeUserStatus")
    class ChangeUserStatus {

        @Test
        @DisplayName("should change status successfully")
        void shouldChangeStatusSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
            when(userRepository.save(any(User.class))).thenReturn(admin);

            UserResponseDto result = adminService.changeUserStatus(1L, "INACTIVE");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when invalid status")
        void shouldThrowWhenInvalidStatus() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

            assertThatThrownBy(() -> adminService.changeUserStatus(1L, "INVALID"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Invalid user status");
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {

        @Test
        @DisplayName("should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

            adminService.deleteUser(1L);

            verify(userRepository).delete(admin);
        }
    }
}
