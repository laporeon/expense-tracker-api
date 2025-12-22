package com.laporeon.expensetracker.helpers;

public class SwaggerConstants {

    // ===== AUTH EXAMPLES =====

    public static final String REGISTER_SUCCESS = """
            {
              "id": "6949a13ea23ea0e5b43d6068",
              "name": "John Doe",
              "email": "johndoe@gmail.com",
              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJleHBlbnNlLXRyYWNrZXItYXBpLWF1dGgiLCJzdWIiOiI2OTQ5YTEzZWEyM2VhMGU1YjQzZDYwNjgiLCJpYXQiOjE3NjY0MzMwODYsImV4cCI6MTc2NjQ0MDI4Nn0.wzEwLBKtzdNwWGxcr6crkhNq6aEWjsbvAqQBTr4aYgM"
            }
            """;

    public static final String LOGIN_SUCCESS = """
            {
              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJleHBlbnNlLXRyYWNrZXItYXBpLWF1dGgiLCJzdWIiOiI2OTQ5YTEzZWEyM2VhMGU1YjQzZDYwNjgiLCJpYXQiOjE3NjY0MzMwODYsImV4cCI6MTc2NjQ0MDI4Nn0.wzEwLBKtzdNwWGxcr6crkhNq6aEWjsbvAqQBTr4aYgM"
            }
            """;

    // ===== USER EXAMPLES =====

    public static final String USER_UPDATE_SUCCESS = """
            {
              "username": "johndoe",
              "email": "johndoe@gmail.com",
              "updatedAt": "2025-12-10T18:08:28.824174472"
            }
            """;

    public static final String USER_NOT_FOUND_ERROR = """
            {
              "status": 404,
              "message": "User not found or inactive",
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
                  "field": "Name",
                  "message": "Name must be between 3 and 255 characters"
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
              "date": "2025-12-12",
              "createdAt": "2025-12-19T13:30:51.912716229",
              "updatedAt": "2025-12-19T13:30:51.912716229"
            }
            """;

    public static final String EXPENSE_UPDATE_SUCCESS = """
            {
              "id": "693c44c3080f9f897b29be50",
              "name": "Prime Video Updated",
              "description": "Amazon Prime annual subscription",
              "amount": 199.90,
              "category": "subscriptions",
              "date": "2025-12-15"
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
                  "date": "2025-12-12"
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
              "message": "Invalid category name 'test'",
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
