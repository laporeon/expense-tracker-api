package com.laporeon.expensetracker.enums;

import java.util.Arrays;

public enum Category {
    FOOD("food"),
    TRANSPORTATION("transportation"),
    HOUSING("housing"),
    UTILITIES("utilities"),
    HEALTHCARE("healthcare"),
    ENTERTAINMENT("entertainment"),
    EDUCATION("education"),
    CLOTHING("clothing"),
    INSURANCE("insurance"),
    SAVINGS("savings"),
    INVESTMENTS("investments"),
    GROCERIES("groceries"),
    PETS("pets"),
    GIFTS("gifts"),
    TRAVEL("travel"),
    SUBSCRIPTIONS("subscriptions"),
    TECHNOLOGY("technology"),
    SPORTS("sports"),
    OTHERS("others");

    private static final String INVALID_CATEGORY_VALUE_ERROR = "Invalid category name '%s'";
    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public static Category fromString(String value) {
        return Arrays.stream(Category.values())
                     .filter(cat -> cat.categoryName.equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(INVALID_CATEGORY_VALUE_ERROR.formatted(value)));
    }

}