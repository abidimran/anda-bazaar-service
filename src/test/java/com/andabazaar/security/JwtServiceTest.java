package com.andabazaar.security;

import com.andabazaar.config.JwtConfig;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.repository.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtService Tests")
class JwtServiceTest {
    private JwtService jwtService;
    private TokenBlacklistService tokenBlacklistService;
    private User user;

    @BeforeEach
    void setUp() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("myTestSecretKeyThatIsLongEnoughForHS256Algorithm123");
        jwtConfig.setExpiration(3600000L);
        tokenBlacklistService = new TokenBlacklistService();
        jwtService = new JwtService(jwtConfig, tokenBlacklistService);
        user = User.builder()
                .id(1L).email("test@example.com").firstName("Test").lastName("User")
                .password("encodedPassword").role(RoleType.USER).status(UserStatus.ACTIVE).build();
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {
        @Test
        @DisplayName("should generate a non-null token")
        void shouldGenerateNonNullToken() {
            assertThat(jwtService.generateToken(user)).isNotNull().isNotBlank();
        }

        @Test
        @DisplayName("should generate different tokens for different users")
        void shouldGenerateDifferentTokens() {
            User other = User.builder().id(2L).email("other@example.com").role(RoleType.ADMIN).status(UserStatus.ACTIVE).build();
            assertThat(jwtService.generateToken(user)).isNotEqualTo(jwtService.generateToken(other));
        }
    }

    @Nested
    @DisplayName("extractEmail")
    class ExtractEmail {
        @Test
        @DisplayName("should extract email from token")
        void shouldExtractEmail() {
            String token = jwtService.generateToken(user);
            assertThat(jwtService.extractEmail(token)).isEqualTo("test@example.com");
        }
    }

    @Nested
    @DisplayName("isTokenValid")
    class IsTokenValid {
        @Test
        @DisplayName("should return true for valid token")
        void shouldReturnTrueForValidToken() {
            String token = jwtService.generateToken(user);
            assertThat(jwtService.isTokenValid(token, user)).isTrue();
        }

        @Test
        @DisplayName("should return false for wrong user")
        void shouldReturnFalseForWrongUser() {
            String token = jwtService.generateToken(user);
            User other = User.builder().id(2L).email("other@example.com").role(RoleType.USER).status(UserStatus.ACTIVE).build();
            assertThat(jwtService.isTokenValid(token, other)).isFalse();
        }

        @Test
        @DisplayName("should return false for expired token")
        void shouldReturnFalseForExpiredToken() {
            JwtConfig expiredConfig = new JwtConfig();
            expiredConfig.setSecret("myTestSecretKeyThatIsLongEnoughForHS256Algorithm123");
            expiredConfig.setExpiration(-1000L);
            JwtService expiredJwtService = new JwtService(expiredConfig, tokenBlacklistService);
            String token = expiredJwtService.generateToken(user);
            assertThat(jwtService.isTokenValid(token, user)).isFalse();
        }

        @Test
        @DisplayName("should return false for invalid token string")
        void shouldReturnFalseForInvalidToken() {
            assertThat(jwtService.isTokenValid("invalid.token.string", user)).isFalse();
        }

        @Test
        @DisplayName("should return false for blacklisted token")
        void shouldReturnFalseForBlacklistedToken() {
            String token = jwtService.generateToken(user);
            tokenBlacklistService.blacklist(token, jwtService.getExpirationTime(token));
            assertThat(jwtService.isTokenValid(token, user)).isFalse();
        }
    }

    @Nested
    @DisplayName("getExpirationTime")
    class GetExpirationTime {
        @Test
        @DisplayName("should return future expiration time")
        void shouldReturnFutureExpiration() {
            String token = jwtService.generateToken(user);
            assertThat(jwtService.getExpirationTime(token)).isGreaterThan(System.currentTimeMillis());
        }
    }
}
