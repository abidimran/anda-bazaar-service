package com.andabazaar.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class PriceCalculationUtil {
    private PriceCalculationUtil() {
    }

    public static BigDecimal calculateChange(BigDecimal previousPrice, BigDecimal currentPrice) {
        if (previousPrice == null
                || currentPrice == null) {
            return BigDecimal.ZERO;
        }

        return currentPrice.subtract(previousPrice);
    }

    public static String calculateChangeType(BigDecimal previousPrice, BigDecimal currentPrice) {
        if (previousPrice == null
                || currentPrice == null) {
            return "NO_CHANGE";
        }

        int result = currentPrice.compareTo(previousPrice);
        if (result > 0) {
            return "INCREASE";
        }

        if (result < 0) {
            return "DECREASE";
        }

        return "NO_CHANGE";
    }

    public static BigDecimal calculatePercentageChange(BigDecimal previousPrice, BigDecimal currentPrice) {
        if (previousPrice == null
                || currentPrice == null
                || previousPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return currentPrice
                .subtract(previousPrice)
                .multiply(BigDecimal.valueOf(100))
                .divide( previousPrice, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateAverage(List<BigDecimal> prices) {
        if (prices == null || prices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<BigDecimal> validPrices = prices.stream()
                        .filter(price -> price != null)
                        .toList();
        if (validPrices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total =
                validPrices.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf( validPrices.size() ), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal findLowestPrice(List<BigDecimal> prices) {
        if (prices == null) {
            return BigDecimal.ZERO;
        }

        return prices.stream()
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public static BigDecimal findHighestPrice(List<BigDecimal> prices) {
        if (prices == null) {
            return BigDecimal.ZERO;
        }

        return prices.stream()
                .filter(price -> price != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
