package com.laporeon.expensetracker.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 255, message = "Name must be between {min} and {max} characters")
        @Schema(example = "John Doe")
        String name,
        @Size(min = 6, max = 25, message = "Username must be between {min} and {max} characters")
        @Schema(description = "Optional username for user registration. If not provided, it can be set later via user update endpoint.", example = "johndoe")
        String username,
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email")
        @NotBlank(message = "Email is required")
        @Schema(example = "johndoe@gmail.com")
        String email,
        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=\\S{8,25}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$",
                message = "Password must be 8-25 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        @Schema(example = "#P4ssword_")
        String password
) {
}
