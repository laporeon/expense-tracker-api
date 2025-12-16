package com.laporeon.expensetracker.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public static Category fromString(String text) {
        return Arrays.stream(Category.values())
                     .filter(c -> c.categoryName.equalsIgnoreCase(text))
                     .findFirst()
                     .orElseThrow(() -> {
                         String availableCategories = Arrays.stream(Category.values())
                                                            .map(c -> c.categoryName)
                                                            .collect(Collectors.joining(", "));
                         return new IllegalArgumentException(
                                 String.format("Invalid category '%s'. Available categories: %s", text, availableCategories));
                     });
    }

}