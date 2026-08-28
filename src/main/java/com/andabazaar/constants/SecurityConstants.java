package com.andabazaar.constants;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String AUTHORIZATION_HEADER =
            "Authorization";

    public static final String BEARER_PREFIX =
            "Bearer ";

    public static final String LOGIN_URL =
            "/api/auth/login";

    public static final String REGISTER_URL =
            "/api/auth/register";

    public static final String REFRESH_TOKEN_URL =
            "/api/auth/refresh";

    public static final String[] PUBLIC_ENDPOINTS = {

            "/api/auth/**",

            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",

            "/api/news/public/**",

            "/api/markets/public/**",

            "/api/egg-prices/public/**"
    };

    public static final String[] ADMIN_ENDPOINTS = {

            "/api/admin/**"
    };
}