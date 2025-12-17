package com.laporeon.expensetracker.helpers;

public class SwaggerConstants {

    // ===== AUTH EXAMPLES =====

    public static final String USER_REGISTER_SUCCESS = """
            {
              "username": "username",
              "email": "user@gmail.com"
            }
            """;

    // ===== USER EXAMPLES =====

    public static final String USER_UPDATE_SUCCESS = """
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
              "error": "Validation Error",
              "message": "Request validation failed for one or more fields",
              "errors": [
                {
                  "field": "email",
                  "message": "Invalid email"
                },
                {
                  "field": "username",
                  "message": "Username must be between 6 and 25 characters"
                },
                {
                  "field": "password",
                  "message": "Password must be 8-25 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
                }
              ],
              "timestamp": "2025-12-17T16:16:25.872529336Z"
            }
            """;

    // ===== EXPENSE EXAMPLES =====

    public static final String EXPENSE_CREATE_SUCCESS = """
            {
              "id": "693c44c3080f9f897b29be50",
              "name": "Prime Video",
              "description": "Amazon Prime subscription",
              "amount": 19.90,
              "category": "subscriptions",
              "expenseDate": "2025-12-12"
            }
            """;

    public static final String EXPENSE_UPDATE_SUCCESS = """
            {
              "id": "693c44c3080f9f897b29be50",
              "name": "Prime Video Updated",
              "description": "Amazon Prime annual subscription",
              "amount": 199.90,
              "category": "subscriptions",
              "expenseDate": "2025-12-15"
            }
            """;

    public static final String EXPENSES_PAGE_SUCCESS = """
            {
              "content": [
                {
                  "id": "693c44c3080f9f897b29be50",
                  "name": "Prime Video",
                  "description": "Amazon Prime subscription",
                  "amount": 19.90,
                  "category": "entertainment",
                  "expenseDate": "2025-12-12"
                }
              ],
              "pageNumber": 0,
              "pageSize": 10,
              "totalPages": 1,
              "totalElements": 1,
              "numberOfElements": 1,
              "isFirstPage": true,
              "isLastPage": true,
              "isEmpty": false,
              "isSorted": true,
              "isUnsorted": false
            }
            """;

    public static final String EXPENSE_NOT_FOUND_ERROR = """
            {
              "status": 404,
              "message": "Expense with id '693c44c3080f9f897b29be34' not found",
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    public static final String EXPENSE_INVALID_BODY_ERROR = """
            {
              "status": 400,
              "error": "Validation Error",
              "message": "Request validation failed for one or more fields",
              "errors": [
                {
                  "field": "amount",
                  "message": "Amount must be greater than 0"
                },
                {
                  "field": "description",
                  "message": "Description must be between 10-50 characters long"
                }
              ],
              "timestamp": "2025-12-17T16:10:43.370769025Z"
            }
            """;

    public static final String INVALID_CATEGORY_ERROR = """
            {
              "status": 422,
              "message": "Invalid category: 'streaming'. Available categories: food, transportation, housing, utilities, healthcare, entertainment, education, clothing, insurance, savings, investments, groceries, pets, gifts, travel, subscriptions, technology, sports, others",
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    // ===== COMMON EXAMPLES =====

    public static final String CONFLICT_ERROR = """
            {
              "status": 409,
              "message": "Email already registered",
              "timestamp": "2025-12-10T18:10:42.133880691Z"
            }
            """;

    public static final String SERVER_ERROR = """
            {
              "status": 500,
              "message": "An unexpected error occurred",
              "timestamp": "2025-12-10T18:08:56.353210281Z"
            }
            """;

}
