package com.andabazaar.security;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.andabazaar.config.JwtConfig;
import com.andabazaar.repository.entity.User;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;

@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("myTestSecretKeyThatIsLongEnoughForHS256Algorithm123");
        jwtConfig.setExpiration(3600000L); // 1 hour

        jwtService = new JwtService(jwtConfig);

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .role(RoleType.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        @DisplayName("should generate a non-null token")
        void shouldGenerateNonNullToken() {
            String token = jwtService.generateToken(user);

            assertThat(token).isNotNull().isNotBlank();
        }

        @Test
        @DisplayName("should generate different tokens for different users")
        void shouldGenerateDifferentTokens() {
            User anotherUser = User.builder()
                    .id(2L).email("another@example.com")
                    .role(RoleType.ADMIN).status(UserStatus.ACTIVE).build();

            String token1 = jwtService.generateToken(user);
            String token2 = jwtService.generateToken(anotherUser);

            assertThat(token1).isNotEqualTo(token2);
        }
    }

    @Nested
    @DisplayName("extractEmail")
    class ExtractEmail {

        @Test
        @DisplayName("should extract email from token")
        void shouldExtractEmail() {
            String token = jwtService.generateToken(user);

            String email = jwtService.extractEmail(token);

            assertThat(email).isEqualTo("test@example.com");
        }
    }

    @Nested
    @DisplayName("isTokenValid")
    class IsTokenValid {

        @Test
        @DisplayName("should return true for valid token")
        void shouldReturnTrueForValidToken() {
            String token = jwtService.generateToken(user);

            boolean valid = jwtService.isTokenValid(token, user);

            assertThat(valid).isTrue();
        }

        @Test
        @DisplayName("should return false for wrong user")
        void shouldReturnFalseForWrongUser() {
            String token = jwtService.generateToken(user);

            User anotherUser = User.builder()
                    .id(2L).email("another@example.com")
                    .role(RoleType.USER).status(UserStatus.ACTIVE).build();

            boolean valid = jwtService.isTokenValid(token, anotherUser);

            assertThat(valid).isFalse();
        }

        @Test
        @DisplayName("should return false for expired token")
        void shouldReturnFalseForExpiredToken() {
            JwtConfig expiredConfig = new JwtConfig();
            expiredConfig.setSecret("myTestSecretKeyThatIsLongEnoughForHS256Algorithm123");
            expiredConfig.setExpiration(-1000L); // already expired

            JwtService expiredJwtService = new JwtService(expiredConfig);
            String token = expiredJwtService.generateToken(user);

            boolean valid = jwtService.isTokenValid(token, user);

            assertThat(valid).isFalse();
        }

        @Test
        @DisplayName("should return false for invalid token string")
        void shouldReturnFalseForInvalidToken() {
            boolean valid = jwtService.isTokenValid("invalid.token.string", user);

            assertThat(valid).isFalse();
        }
    }
}
