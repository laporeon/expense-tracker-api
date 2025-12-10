package com.laporeon.registrationsystem.helpers;

public class SwaggerConstants {

    public static final String USER_RESPONSE_EXAMPLE = """
            {
              "id": "6939b71c96d47dfc0e108ceb",
              "username": "username",
              "email": "user@gmail.com",
              "createdAt": "2025-12-10T18:08:28.824174472",
              "updatedAt": "2025-12-10T18:08:28.824174472"
            }
            """;

    public static final String VALIDATION_ERROR_EXAMPLE = """
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
