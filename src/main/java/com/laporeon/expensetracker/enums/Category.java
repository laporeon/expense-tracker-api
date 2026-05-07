package com.laporeon.expensetracker.enums;

import java.util.Arrays;

public enum Category {
    FOOD,
    TRANSPORTATION,
    HOUSING,
    UTILITIES,
    HEALTHCARE,
    ENTERTAINMENT,
    EDUCATION,
    CLOTHING,
    INSURANCE,
    SAVINGS,
    INVESTMENTS,
    GROCERIES,
    PETS,
    GIFTS,
    TRAVEL,
    SUBSCRIPTIONS,
    TECHNOLOGY,
    SPORTS,
    OTHERS;

    private static final String INVALID_CATEGORY_VALUE_ERROR = "Invalid category name '%s'";

    public static Category fromString(String value) {
        return Arrays.stream(Category.values())
                     .filter(cat -> cat.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(INVALID_CATEGORY_VALUE_ERROR.formatted(value)));
    }
}