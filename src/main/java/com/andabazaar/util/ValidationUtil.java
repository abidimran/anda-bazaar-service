package com.andabazaar.util;

import java.util.regex.Pattern;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    private static final Pattern PINCODE_PATTERN =
            Pattern.compile("^[0-9]{6}$");

    public static boolean isNullOrEmpty( String value) {

        return value == null
                || value.trim().isEmpty();
    }

    public static boolean isValidEmail( String email) {

        return email != null
                && EMAIL_PATTERN.matcher(email)
                        .matches();
    }

    public static boolean isValidPhone( String phone) {

        return phone != null
                && PHONE_PATTERN.matcher(phone)
                        .matches();
    }

    public static boolean isValidPincode( String pincode) {

        return pincode != null
                && PINCODE_PATTERN.matcher(pincode)
                        .matches();
    }

    public static boolean isPositive( Number value) {

        return value != null
                && value.doubleValue() > 0;
    }

    public static boolean isNonNegative( Number value) {

        return value != null
                && value.doubleValue() >= 0;
    }

    public static boolean isValidId( Long id) {

        return id != null && id > 0;
    }
}