package com.andabazaar.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilTest {
    // =============================================================
    // today / yesterday / tomorrow
    // =============================================================
    @Test
    @DisplayName("today() returns current date")
    void todayReturnsCurrentDate() {
        assertEquals(LocalDate.now(), DateTimeUtil.today());
    }

    @Test
    @DisplayName("yesterday() returns previous day")
    void yesterdayReturnsPreviousDay() {
        assertEquals(LocalDate.now().minusDays(1), DateTimeUtil.yesterday());
    }

    @Test
    @DisplayName("tomorrow() returns next day")
    void tomorrowReturnsNextDay() {
        assertEquals(LocalDate.now().plusDays(1), DateTimeUtil.tomorrow());
    }

    // =============================================================
    // now
    // =============================================================
    @Test
    @DisplayName("now() returns current date-time close to system clock")
    void nowReturnsCurrentDateTime() {
        LocalDateTime before = LocalDateTime.now();
        LocalDateTime result = DateTimeUtil.now();
        LocalDateTime after = LocalDateTime.now();
        assertFalse(result.isBefore(before));
        assertFalse(result.isAfter(after));
    }

    // =============================================================
    // startOfDay / endOfDay
    // =============================================================
    @Nested
    @DisplayName("startOfDay / endOfDay")
    class DayBoundaryTests {
        @Test
        @DisplayName("startOfDay returns midnight")
        void startOfDayReturnsMidnight() {
            LocalDate date =
                    LocalDate.of(2025, 6, 15);
            LocalDateTime result =
                    DateTimeUtil.startOfDay(date);
            assertEquals( date.atStartOfDay(), result);
            assertEquals(0, result.getHour());
            assertEquals(0, result.getMinute());
            assertEquals(0, result.getSecond());
        }

        @Test
        @DisplayName("endOfDay returns last moment of day")
        void endOfDayReturnsLastMoment() {
            LocalDate date =
                    LocalDate.of(2025, 6, 15);
            LocalDateTime result =
                    DateTimeUtil.endOfDay(date);
            assertEquals( date.atTime(LocalTime.MAX), result);
            assertEquals(23, result.getHour());
            assertEquals(59, result.getMinute());
            assertEquals(59, result.getSecond());
        }
    }

    // =============================================================
    // isToday
    // =============================================================
    @Nested
    @DisplayName("isToday")
    class IsTodayTests {
        @Test
        @DisplayName("returns true for today's date")
        void returnsTrueForToday() {
            assertTrue(DateTimeUtil.isToday(LocalDate.now()));
        }

        @Test
        @DisplayName("returns false for yesterday")
        void returnsFalseForYesterday() {
            assertFalse(DateTimeUtil.isToday(LocalDate.now() .minusDays(1)));
        }

        @Test
        @DisplayName("returns false for tomorrow")
        void returnsFalseForTomorrow() {
            assertFalse(DateTimeUtil.isToday(LocalDate.now() .plusDays(1)));
        }
    }

    // =============================================================
    // isPast
    // =============================================================
    @Nested
    @DisplayName("isPast")
    class IsPastTests {
        @Test
        @DisplayName("returns true for past date")
        void returnsTrueForPast() {
            assertTrue(DateTimeUtil.isPast(LocalDate.now() .minusDays(7)));
        }

        @Test
        @DisplayName("returns false for today")
        void returnsFalseForToday() {
            assertFalse(DateTimeUtil.isPast(LocalDate.now()));
        }

        @Test
        @DisplayName("returns false for future date")
        void returnsFalseForFuture() {
            assertFalse(DateTimeUtil.isPast(LocalDate.now() .plusDays(1)));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(DateTimeUtil.isPast(null));
        }
    }

    // =============================================================
    // isFuture
    // =============================================================
    @Nested
    @DisplayName("isFuture")
    class IsFutureTests {
        @Test
        @DisplayName("returns true for future date")
        void returnsTrueForFuture() {
            assertTrue(DateTimeUtil.isFuture(LocalDate.now() .plusDays(7)));
        }

        @Test
        @DisplayName("returns false for today")
        void returnsFalseForToday() {
            assertFalse(DateTimeUtil.isFuture(LocalDate.now()));
        }

        @Test
        @DisplayName("returns false for past date")
        void returnsFalseForPast() {
            assertFalse(DateTimeUtil.isFuture(LocalDate.now() .minusDays(1)));
        }

        @Test
        @DisplayName("returns false for null")
        void returnsFalseForNull() {
            assertFalse(DateTimeUtil.isFuture(null));
        }
    }
}
