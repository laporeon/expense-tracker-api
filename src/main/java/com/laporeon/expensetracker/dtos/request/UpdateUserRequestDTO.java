package com.laporeon.expensetracker.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(
        @Size(min = 3, max = 255, message = "Name must be between {min} and {max} characters")
        @Schema(
                description = "Optional name",
                example = "John James Doe"
        )
        String name,
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email")
        @Schema(
                description = "Optional email. Must be a valid email address.",
                example = "user@gmail.com"
        )
        String email,
        @Pattern(
                regexp = "^(?=\\S{8,25}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$",
                message = "Password must be 8-25 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        @Schema(
                description = "Optional password. Must follow the defined complexity rules.",
                example = "#P4ssword_"
        )
        String password
) {
}
