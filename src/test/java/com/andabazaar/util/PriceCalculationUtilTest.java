package com.andabazaar.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriceCalculationUtilTest {
    // =============================================================
    // calculateChange
    // =============================================================
    @Nested
    @DisplayName("calculateChange")
    class CalculateChangeTests {
        @Test
        @DisplayName("returns positive change when price increased")
        void returnsPositiveChange() {
            BigDecimal previous = new BigDecimal("5.00");
            BigDecimal current = new BigDecimal("7.50");
            BigDecimal result =
                    PriceCalculationUtil.calculateChange( previous, current);
            assertEquals( new BigDecimal("2.50"), result);
        }

        @Test
        @DisplayName("returns negative change when price decreased")
        void returnsNegativeChange() {
            BigDecimal previous = new BigDecimal("10.00");
            BigDecimal current = new BigDecimal("8.00");
            BigDecimal result =
                    PriceCalculationUtil.calculateChange( previous, current);
            assertEquals( new BigDecimal("-2.00"), result);
        }

        @Test
        @DisplayName("returns zero when prices are equal")
        void returnsZeroWhenEqual() {
            BigDecimal price = new BigDecimal("5.00");
            BigDecimal result =
                    PriceCalculationUtil.calculateChange( price, price);
            assertEquals( BigDecimal.ZERO.setScale(2), result);
        }

        @Test
        @DisplayName("returns zero when previous price is null")
        void returnsZeroWhenPreviousNull() {
            BigDecimal result =
                    PriceCalculationUtil.calculateChange( null, new BigDecimal("5.00"));
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero when current price is null")
        void returnsZeroWhenCurrentNull() {
            BigDecimal result =
                    PriceCalculationUtil.calculateChange( new BigDecimal("5.00"), null);
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero when both prices are null")
        void returnsZeroWhenBothNull() {
            BigDecimal result =
                    PriceCalculationUtil.calculateChange( null, null);
            assertEquals(BigDecimal.ZERO, result);
        }
    }

    // =============================================================
    // calculateChangeType
    // =============================================================
    @Nested
    @DisplayName("calculateChangeType")
    class CalculateChangeTypeTests {
        @Test
        @DisplayName("returns INCREASE when current > previous")
        void returnsIncrease() {
            String result =
                    PriceCalculationUtil.calculateChangeType( new BigDecimal("5.00"), new BigDecimal("8.00"));
            assertEquals("INCREASE", result);
        }

        @Test
        @DisplayName("returns DECREASE when current < previous")
        void returnsDecrease() {
            String result =
                    PriceCalculationUtil.calculateChangeType( new BigDecimal("8.00"), new BigDecimal("5.00"));
            assertEquals("DECREASE", result);
        }

        @Test
        @DisplayName("returns NO_CHANGE when prices are equal")
        void returnsNoChange() {
            String result =
                    PriceCalculationUtil.calculateChangeType( new BigDecimal("5.00"), new BigDecimal("5.00"));
            assertEquals("NO_CHANGE", result);
        }

        @Test
        @DisplayName("returns NO_CHANGE when previous is null")
        void returnsNoChangeWhenPreviousNull() {
            String result =
                    PriceCalculationUtil.calculateChangeType( null, new BigDecimal("5.00"));
            assertEquals("NO_CHANGE", result);
        }

        @Test
        @DisplayName("returns NO_CHANGE when current is null")
        void returnsNoChangeWhenCurrentNull() {
            String result =
                    PriceCalculationUtil.calculateChangeType( new BigDecimal("5.00"), null);
            assertEquals("NO_CHANGE", result);
        }
    }

    // =============================================================
    // calculatePercentageChange
    // =============================================================
    @Nested
    @DisplayName("calculatePercentageChange")
    class CalculatePercentageChangeTests {
        @Test
        @DisplayName("calculates correct percentage increase")
        void calculatesPercentageIncrease() {
            BigDecimal result =
                    PriceCalculationUtil.calculatePercentageChange( new BigDecimal("100.00"), new BigDecimal("125.00"));
            assertEquals( new BigDecimal("25.00"), result);
        }

        @Test
        @DisplayName("calculates correct percentage decrease")
        void calculatesPercentageDecrease() {
            BigDecimal result =
                    PriceCalculationUtil.calculatePercentageChange( new BigDecimal("200.00"), new BigDecimal("150.00"));
            assertEquals( new BigDecimal("-25.00"), result);
        }

        @Test
        @DisplayName("returns zero when previous is null")
        void returnsZeroWhenPreviousNull() {
            BigDecimal result =
                    PriceCalculationUtil.calculatePercentageChange( null, new BigDecimal("5.00"));
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero when previous price is zero")
        void returnsZeroWhenPreviousPriceIsZero() {
            BigDecimal result =
                    PriceCalculationUtil.calculatePercentageChange( BigDecimal.ZERO, new BigDecimal("5.00"));
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero percentage when prices equal")
        void returnsZeroWhenEqual() {
            BigDecimal result =
                    PriceCalculationUtil.calculatePercentageChange( new BigDecimal("50.00"), new BigDecimal("50.00"));
            assertEquals( new BigDecimal("0.00"), result);
        }
    }

    // =============================================================
    // calculateAverage
    // =============================================================
    @Nested
    @DisplayName("calculateAverage")
    class CalculateAverageTests {
        @Test
        @DisplayName("calculates average of multiple prices")
        void calculatesAverage() {
            List<BigDecimal> prices = Arrays.asList( new BigDecimal("10.00"), new BigDecimal("20.00"), new BigDecimal("30.00"));
            BigDecimal result =
                    PriceCalculationUtil.calculateAverage( prices);
            assertEquals( new BigDecimal("20.00"), result);
        }

        @Test
        @DisplayName("returns zero for null list")
        void returnsZeroForNull() {
            BigDecimal result =
                    PriceCalculationUtil.calculateAverage( null);
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero for empty list")
        void returnsZeroForEmpty() {
            BigDecimal result =
                    PriceCalculationUtil.calculateAverage( Collections.emptyList());
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("skips null values in list")
        void skipsNullValues() {
            List<BigDecimal> prices = Arrays.asList( new BigDecimal("10.00"), null, new BigDecimal("20.00"));
            BigDecimal result =
                    PriceCalculationUtil.calculateAverage( prices);
            assertEquals( new BigDecimal("15.00"), result);
        }

        @Test
        @DisplayName("returns zero when list has only nulls")
        void returnsZeroWhenAllNull() {
            List<BigDecimal> prices =
                    Arrays.asList(null, null);
            BigDecimal result =
                    PriceCalculationUtil.calculateAverage( prices);
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("handles single element list")
        void handlesSingleElement() {
            List<BigDecimal> prices =
                    List.of(new BigDecimal("42.50"));
            BigDecimal result =
                    PriceCalculationUtil.calculateAverage( prices);
            assertEquals( new BigDecimal("42.50"), result);
        }
    }

    // =============================================================
    // findLowestPrice
    // =============================================================
    @Nested
    @DisplayName("findLowestPrice")
    class FindLowestPriceTests {
        @Test
        @DisplayName("finds lowest price in list")
        void findsLowest() {
            List<BigDecimal> prices = Arrays.asList( new BigDecimal("15.00"), new BigDecimal("5.00"), new BigDecimal("25.00"));
            BigDecimal result =
                    PriceCalculationUtil.findLowestPrice( prices);
            assertEquals( new BigDecimal("5.00"), result);
        }

        @Test
        @DisplayName("returns zero for null list")
        void returnsZeroForNull() {
            BigDecimal result =
                    PriceCalculationUtil.findLowestPrice( null);
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero for empty list")
        void returnsZeroForEmpty() {
            BigDecimal result =
                    PriceCalculationUtil.findLowestPrice( Collections.emptyList());
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("skips null values")
        void skipsNulls() {
            List<BigDecimal> prices = Arrays.asList( null, new BigDecimal("10.00"), null, new BigDecimal("3.00"));
            BigDecimal result =
                    PriceCalculationUtil.findLowestPrice( prices);
            assertEquals( new BigDecimal("3.00"), result);
        }
    }

    // =============================================================
    // findHighestPrice
    // =============================================================
    @Nested
    @DisplayName("findHighestPrice")
    class FindHighestPriceTests {
        @Test
        @DisplayName("finds highest price in list")
        void findsHighest() {
            List<BigDecimal> prices = Arrays.asList( new BigDecimal("15.00"), new BigDecimal("5.00"), new BigDecimal("25.00"));
            BigDecimal result =
                    PriceCalculationUtil.findHighestPrice( prices);
            assertEquals( new BigDecimal("25.00"), result);
        }

        @Test
        @DisplayName("returns zero for null list")
        void returnsZeroForNull() {
            BigDecimal result =
                    PriceCalculationUtil.findHighestPrice( null);
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("returns zero for empty list")
        void returnsZeroForEmpty() {
            BigDecimal result =
                    PriceCalculationUtil.findHighestPrice( Collections.emptyList());
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("skips null values")
        void skipsNulls() {
            List<BigDecimal> prices = Arrays.asList( null, new BigDecimal("10.00"), null, new BigDecimal("50.00"));
            BigDecimal result =
                    PriceCalculationUtil.findHighestPrice( prices);
            assertEquals( new BigDecimal("50.00"), result);
        }
    }
}
