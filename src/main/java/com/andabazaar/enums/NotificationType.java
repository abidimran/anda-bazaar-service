package com.andabazaar.enums;

public enum NotificationType {

    // =========================================================
    // SUBSCRIPTION
    // =========================================================

    SUBSCRIPTION_PURCHASED,

    SUBSCRIPTION_ACTIVATED,

    SUBSCRIPTION_EXPIRING,

    SUBSCRIPTION_EXPIRED,

    SUBSCRIPTION_CANCELLED,


    // =========================================================
    // PAYMENT
    // =========================================================

    PAYMENT_SUCCESS,

    PAYMENT_FAILED,

    PAYMENT_PENDING,


    // =========================================================
    // PRICE ALERT
    // =========================================================

    PRICE_ALERT,

    PRICE_INCREASE,

    PRICE_DECREASE,


    // =========================================================
    // PRICE REPORT
    // =========================================================

    PRICE_REPORT_SUBMITTED,

    PRICE_REPORT_APPROVED,

    PRICE_REPORT_REJECTED,


    // =========================================================
    // MARKET
    // =========================================================

    MARKET_ADDED,

    MARKET_UPDATED,


    // =========================================================
    // GENERAL
    // =========================================================

    GENERAL,

    SYSTEM,

    PROMOTION
}