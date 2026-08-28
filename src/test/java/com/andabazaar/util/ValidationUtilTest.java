package com.andabazaar.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    // =============================================================
    // isNullOrEmpty
    // =============================================================

    @Nested
    @DisplayName("isNullOrEmpty")
    class IsNullOrEmptyTests {

        @Test
        @DisplayName("returns true for null")
        void returnsTrueForNull() {
            assertTrue(ValidationUtil.isNullOrEmpty(null));
        }

        @Test
        @DisplayName("returns true for empty string")
        void returnsTrueForEmpty() {
            assertTrue(ValidationUtil.isNullOrEmpty(""));
        }

        @Test
        @DisplayName("returns true for whitespace only")
        void returnsTrueForWhitespace() {
            assertTrue(ValidationUtil.isNullOrEmpty("   "));
        }

        @Test
        @DisplayName("returns false for non-empty string")
        void returnsFalseForNonEmpty() {
            assertFalse(ValidationUtil.isNullOrEmpty("hello"));
        }
    }

    // =============================================================
    // isValidEmail
    // =============================================================

    @Nested
    @DisplayName("isValidEmail")
    class IsValidEmailTests {

        @Test
        @DisplayName("returns true for valid email")
        void returnsTrueForValid() {
            assertTrue(
                    ValidationUtil.isValidEmail(
                            "user@example.com"));
        }

        @Test
        @DisplayName("returns true for email with dots and plus")
        void returnsTrueForDotsAndPlus() {
            assertTrue(
                    ValidationUtil.isValidEmail(
                            "user.name+tag@domain.co.in"));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(
                    ValidationUtil.isValidEmail(null));
        }

        @Test
        @DisplayName("returns false for empty string")
        void returnsFalseForEmpty() {
            assertFalse(
                    ValidationUtil.isValidEmail(""));
        }

        @Test
        @DisplayName("returns false for missing @")
        void returnsFalseForMissingAt() {
            assertFalse(
                    ValidationUtil.isValidEmail(
                            "userexample.com"));
        }

        @Test
        @DisplayName("returns false for missing domain")
        void returnsFalseForMissingDomain() {
            assertFalse(
                    ValidationUtil.isValidEmail(
                            "user@"));
        }
    }

    // =============================================================
    // isValidPhone
    // =============================================================

    @Nested
    @DisplayName("isValidPhone")
    class IsValidPhoneTests {

        @Test
        @DisplayName("returns true for 10 digit phone")
        void returnsTrueForValid() {
            assertTrue(
                    ValidationUtil.isValidPhone(
                            "9876543210"));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(
                    ValidationUtil.isValidPhone(null));
        }

        @Test
        @DisplayName("returns false for less than 10 digits")
        void returnsFalseForShort() {
            assertFalse(
                    ValidationUtil.isValidPhone(
                            "12345"));
        }

        @Test
        @DisplayName("returns false for more than 10 digits")
        void returnsFalseForLong() {
            assertFalse(
                    ValidationUtil.isValidPhone(
                            "12345678901"));
        }

        @Test
        @DisplayName("returns false for non-digit characters")
        void returnsFalseForNonDigits() {
            assertFalse(
                    ValidationUtil.isValidPhone(
                            "98765-4321"));
        }
    }

    // =============================================================
    // isValidPincode
    // =============================================================

    @Nested
    @DisplayName("isValidPincode")
    class IsValidPincodeTests {

        @Test
        @DisplayName("returns true for 6 digit pincode")
        void returnsTrueForValid() {
            assertTrue(
                    ValidationUtil.isValidPincode(
                            "400001"));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(
                    ValidationUtil.isValidPincode(null));
        }

        @Test
        @DisplayName("returns false for 5 digits")
        void returnsFalseForShort() {
            assertFalse(
                    ValidationUtil.isValidPincode(
                            "40001"));
        }

        @Test
        @DisplayName("returns false for 7 digits")
        void returnsFalseForLong() {
            assertFalse(
                    ValidationUtil.isValidPincode(
                            "4000012"));
        }

        @Test
        @DisplayName("returns false for non-digit characters")
        void returnsFalseForNonDigits() {
            assertFalse(
                    ValidationUtil.isValidPincode(
                            "40A001"));
        }
    }

    // =============================================================
    // isPositive
    // =============================================================

    @Nested
    @DisplayName("isPositive")
    class IsPositiveTests {

        @Test
        @DisplayName("returns true for positive number")
        void returnsTrueForPositive() {
            assertTrue(ValidationUtil.isPositive(5));
            assertTrue(ValidationUtil.isPositive(0.01));
        }

        @Test
        @DisplayName("returns false for zero")
        void returnsFalseForZero() {
            assertFalse(ValidationUtil.isPositive(0));
        }

        @Test
        @DisplayName("returns false for negative number")
        void returnsFalseForNegative() {
            assertFalse(ValidationUtil.isPositive(-1));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(ValidationUtil.isPositive(null));
        }
    }

    // =============================================================
    // isNonNegative
    // =============================================================

    @Nested
    @DisplayName("isNonNegative")
    class IsNonNegativeTests {

        @Test
        @DisplayName("returns true for positive")
        void returnsTrueForPositive() {
            assertTrue(
                    ValidationUtil.isNonNegative(5));
        }

        @Test
        @DisplayName("returns true for zero")
        void returnsTrueForZero() {
            assertTrue(
                    ValidationUtil.isNonNegative(0));
        }

        @Test
        @DisplayName("returns false for negative")
        void returnsFalseForNegative() {
            assertFalse(
                    ValidationUtil.isNonNegative(-1));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(
                    ValidationUtil.isNonNegative(null));
        }
    }

    // =============================================================
    // isValidId
    // =============================================================

    @Nested
    @DisplayName("isValidId")
    class IsValidIdTests {

        @Test
        @DisplayName("returns true for positive long")
        void returnsTrueForPositive() {
            assertTrue(ValidationUtil.isValidId(1L));
            assertTrue(ValidationUtil.isValidId(999L));
        }

        @Test
        @DisplayName("returns false for zero")
        void returnsFalseForZero() {
            assertFalse(ValidationUtil.isValidId(0L));
        }

        @Test
        @DisplayName("returns false for negative")
        void returnsFalseForNegative() {
            assertFalse(ValidationUtil.isValidId(-1L));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(ValidationUtil.isValidId(null));
        }
    }
}
