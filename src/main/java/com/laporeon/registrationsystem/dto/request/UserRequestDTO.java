package com.laporeon.registrationsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 6, max = 25, message = "Username must be between {min} and {max} characters")
        String username,
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email")
        @NotBlank(message = "Email is required")
        String email
) {
}
