package com.laporeon.expensetracker.helpers;

public class SwaggerConstants {

    // ===== AUTH EXAMPLES =====

    public static final String REGISTER_USER_SUCCESS_RESPONSE = """
            {
              "username": "username",
              "email": "user@gmail.com"
            }
            """;

    // ===== USER EXAMPLES =====

    public static final String UPDATE_USER_SUCCESS_RESPONSE = """
            {
              "username": "username",
              "email": "user@gmail.com",
              "updatedAt": "2025-12-10T18:08:28.824174472"
            }
            """;

    public static final String USER_NOT_FOUND_ERROR = """
            {
              "status": 404,
              "message": "User not found",
              "timestamp": "2025-12-11T18:02:20.152685443Z"
            }
            """;

    public static final String USER_INVALID_BODY_ERROR = """
            {
              "status": 400,
              "message": "Validation failed",
              "details": {
                "email": "Invalid email",
                "username": "Username must be between 6 and 25 characters",
                "password": "Password must be 8-25 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
              },
              "timestamp": "2025-12-10T18:08:56.353210281Z"
            }
            """;

    // ===== EXPENSE EXAMPLES =====

    public static final String EXPENSE_CREATED_RESPONSE = """
            {
              "id": "693c44c3080f9f897b29be50",
              "name": "Prime Video",
              "description": "Amazon Prime subscription",
              "amount": 19.90,
              "category": "streaming",
              "date": "2025-12-12"
            }
            """;

    public static final String EXPENSE_INVALID_BODY_ERROR = """
            {
              "status": 400,
              "message": "Validation failed",
              "details": {
                "date": "Expense date is required (format: yyyy-MM-dd)",
                "amount": "Amount must be greater than 0",
                "name": "Name must be between 3-25 characters long",
                "description": "Description must be between 10-50 characters long"
              },
              "timestamp": "2025-12-12T16:36:02.562839957Z"
            }
            """;

    // ===== CATEGORY EXAMPLES =====

    public static final String CATEGORY_CREATED_RESPONSE = """
            {
              "name": "health"
            }
            """;

    public static final String CATEGORY_INVALID_BODY_ERROR = """
            {
              "status": 400,
              "message": "Validation failed",
              "details": {
                "name": "Category name must be between 3-15 characters long"
              },
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    public static final String CATEGORY_NOT_FOUND_ERROR = """
            {
              "status": 404,
              "message": "Category not found. Check for available categories on: /categories",
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    public static final String CATEGORY_ALREADY_REGISTERED_ERROR = """
            {
              "status": 409,
              "message": "Category already registered",
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    public static final String LIST_CATEGORIES_RESPONSE = """
            [
                { "name": "health" },
                { "name": "food" },
                { "name": "others" }
            ]
            """;

    // ===== COMMON EXAMPLES =====

    public static final String ALREADY_REGISTERED_ERROR_EXAMPLE = """
            {
              "status": 409,
              "message": "Email already registered",
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    public static final String GENERIC_ERROR_EXAMPLE = """
            {
              "status": 500,
              "message": "An unexpected error occurred",
              "timestamp": "2025-12-10T18:08:56.353210281Z"
            }
            """;

}
