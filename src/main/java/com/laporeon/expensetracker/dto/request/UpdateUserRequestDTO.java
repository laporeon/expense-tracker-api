package com.laporeon.expensetracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequestDTO(
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
