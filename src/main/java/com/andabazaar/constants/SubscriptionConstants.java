package com.andabazaar.constants;

public final class SubscriptionConstants {

    private SubscriptionConstants() {
    }

    public static final int DEFAULT_DURATION_DAYS = 30;

    public static final int MIN_DURATION_DAYS = 1;

    public static final int MAX_DURATION_DAYS = 3650;

    public static final int PRICE_HISTORY_FREE_DAYS = 2;

    public static final String STATUS_PENDING =
            "PENDING";

    public static final String STATUS_ACTIVE =
            "ACTIVE";

    public static final String STATUS_EXPIRED =
            "EXPIRED";

    public static final String STATUS_CANCELLED =
            "CANCELLED";

    public static final String PLAN_MONTHLY =
            "MONTHLY";

    public static final String PLAN_QUARTERLY =
            "QUARTERLY";

    public static final String PLAN_YEARLY =
            "YEARLY";
}