package com.andabazaar.serviceimpl;

import com.andabazaar.dto.auth.LoginRequestDto;
import com.andabazaar.dto.auth.LoginResponseDto;
import com.andabazaar.dto.auth.RegisterRequestDto;
import com.andabazaar.dto.user.UserResponseDto;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.mapper.UserMapper;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.entity.User;
import com.andabazaar.security.JwtService;
import com.andabazaar.service.NotificationService;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .firstName("Imran")
                .lastName("Abid")
                .email("imran@example.com")
                .phone("9876543210")
                .password("encoded_password")
                .role(RoleType.USER)
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
    }

    // =============================================================
    // REGISTER
    // =============================================================
    @Nested
    @DisplayName("register")
    class RegisterTests {
        private RegisterRequestDto registerRequest;
        @BeforeEach
        void setUp() {
            registerRequest = RegisterRequestDto.builder()
                    .firstName("Imran")
                    .lastName("Abid")
                    .email("  Imran@Example.COM  ")
                    .phone("9876543210")
                    .password("password123")
                    .preferredLanguage("en")
                    .preferredCity("Mumbai")
                    .build();
        }

        @Test
        @DisplayName("registers successfully with valid data")
        void registersSuccessfully() {
            when(userRepository.existsByEmail( "imran@example.com"))
                    .thenReturn(false);
            when(userRepository.existsByPhone( "9876543210"))
                    .thenReturn(false);
            when(passwordEncoder.encode("password123"))
                    .thenReturn("encoded_password");
            when(userRepository.save(any(User.class)))
                    .thenReturn(testUser);
            UserResponseDto result =
                    authService.register(registerRequest);
            assertNotNull(result);
            assertEquals("Imran", result.getFirstName());
            assertEquals("Abid", result.getLastName());
            assertEquals( "imran@example.com", result.getEmail());
            assertEquals(RoleType.USER, result.getRole());
            assertEquals( UserStatus.ACTIVE, result.getStatus());
            verify(userRepository).save(any(User.class));
            verify(notificationService)
                    .createNotification(any());
        }

        @Test
        @DisplayName("normalizes email to lowercase and trims")
        void normalizesEmail() {
            when(userRepository.existsByEmail( "imran@example.com"))
                    .thenReturn(false);
            when(userRepository.existsByPhone( "9876543210"))
                    .thenReturn(false);
            when(passwordEncoder.encode(anyString()))
                    .thenReturn("encoded_password");
            when(userRepository.save(any(User.class)))
                    .thenReturn(testUser);
            authService.register(registerRequest);
            verify(userRepository)
                    .existsByEmail("imran@example.com");
        }

        @Test
        @DisplayName("throws BadRequestException for duplicate email")
        void throwsForDuplicateEmail() {
            when(userRepository.existsByEmail( "imran@example.com"))
                    .thenReturn(true);
            BadRequestException exception =
                    assertThrows( BadRequestException.class, () -> authService.register( registerRequest));
            assertEquals( "Email already registered", exception.getMessage());
            verify(userRepository, never())
                    .save(any(User.class));
        }

        @Test
        @DisplayName("throws BadRequestException for duplicate phone")
        void throwsForDuplicatePhone() {
            when(userRepository.existsByEmail( "imran@example.com"))
                    .thenReturn(false);
            when(userRepository.existsByPhone( "9876543210"))
                    .thenReturn(true);
            BadRequestException exception =
                    assertThrows( BadRequestException.class, () -> authService.register( registerRequest));
            assertEquals( "Phone number already registered", exception.getMessage());
            verify(userRepository, never())
                    .save(any(User.class));
        }

        @Test
        @DisplayName("registration succeeds even if notification fails")
        void succeedsEvenIfNotificationFails() {
            when(userRepository.existsByEmail( "imran@example.com"))
                    .thenReturn(false);
            when(userRepository.existsByPhone( "9876543210"))
                    .thenReturn(false);
            when(passwordEncoder.encode(anyString()))
                    .thenReturn("encoded_password");
            when(userRepository.save(any(User.class)))
                    .thenReturn(testUser);
            when(notificationService .createNotification(any()))
                    .thenThrow( new RuntimeException( "Notification failed"));
            UserResponseDto result =
                    authService.register(registerRequest);
            assertNotNull(result);
            assertEquals( "imran@example.com", result.getEmail());
        }
    }

    // =============================================================
    // LOGIN
    // =============================================================
    @Nested
    @DisplayName("login")
    class LoginTests {
        private LoginRequestDto loginRequest;
        @BeforeEach
        void setUp() {
            loginRequest = LoginRequestDto.builder()
                    .email("  Imran@Example.COM  ")
                    .password("password123")
                    .build();
        }

        @Test
        @DisplayName("logs in successfully with valid credentials")
        void logsInSuccessfully() {
            when(userRepository.findByEmail( "imran@example.com"))
                    .thenReturn(Optional.of(testUser));
            when(jwtService.generateToken(testUser))
                    .thenReturn("jwt-token-123");
            LoginResponseDto result =
                    authService.login(loginRequest);
            assertNotNull(result);
            assertEquals("jwt-token-123", result.getToken());
            assertEquals("Bearer", result.getTokenType());
            assertEquals(1L, result.getUserId());
            assertEquals("Imran", result.getFirstName());
            assertEquals( "imran@example.com", result.getEmail());
            assertEquals(RoleType.USER, result.getRole());
            verify(authenticationManager).authenticate( any(UsernamePasswordAuthenticationToken .class));
        }

        @Test
        @DisplayName("throws when authentication fails")
        void throwsForBadCredentials() {
            when(authenticationManager.authenticate(any()))
                    .thenThrow( new BadCredentialsException( "Bad credentials"));
            assertThrows( BadCredentialsException.class, () -> authService.login(loginRequest));
        }

        @Test
        @DisplayName("throws when user not found after authentication")
        void throwsWhenUserNotFound() {
            when(userRepository.findByEmail( "imran@example.com"))
                    .thenReturn(Optional.empty());
            assertThrows( BadRequestException.class, () -> authService.login(loginRequest));
        }

        @Test
        @DisplayName("throws when user account is not active")
        void throwsWhenUserInactive() {
            testUser.setStatus(UserStatus.BLOCKED);
            when(userRepository.findByEmail( "imran@example.com"))
                    .thenReturn(Optional.of(testUser));
            BadRequestException exception =
                    assertThrows( BadRequestException.class, () -> authService.login( loginRequest));
            assertEquals( "User account is not active", exception.getMessage());
        }
    }

    // =============================================================
    // GET CURRENT USER
    // =============================================================
    @Nested
    @DisplayName("getCurrentUser")
    class GetCurrentUserTests {
        @Test
        @DisplayName("returns current user for valid email")
        void returnsCurrentUser() {
            when(userRepository.findByEmail( "imran@example.com"))
                    .thenReturn(Optional.of(testUser));
            UserResponseDto result =
                    authService.getCurrentUser( " Imran@Example.COM ");
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals( "imran@example.com", result.getEmail());
        }

        @Test
        @DisplayName("throws when user not found")
        void throwsWhenNotFound() {
            when(userRepository.findByEmail( "unknown@example.com"))
                    .thenReturn(Optional.empty());
            assertThrows( BadRequestException.class, () -> authService.getCurrentUser( "unknown@example.com"));
        }

        @Test
        @DisplayName("throws when user account is not active")
        void throwsWhenInactive() {
            testUser.setStatus(UserStatus.SUSPENDED);
            when(userRepository.findByEmail( "imran@example.com"))
                    .thenReturn(Optional.of(testUser));
            BadRequestException exception =
                    assertThrows( BadRequestException.class, () -> authService.getCurrentUser( "imran@example.com"));
            assertEquals( "User account is not active", exception.getMessage());
        }
    }
}
