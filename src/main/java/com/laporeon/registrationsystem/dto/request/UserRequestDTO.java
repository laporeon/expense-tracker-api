package com.laporeon.registrationsystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 6, max = 25, message = "Username must be between {min} and {max} characters")
        @Schema(example = "username")
        String username,
        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=\\S{8,25}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$",
                message = "Password must be 8-25 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        @Schema(example = "#P4ssword_")
        String password,
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email")
        @NotBlank(message = "Email is required")
        @Schema(example = "user@gmail.com")
        String email
) {
}
