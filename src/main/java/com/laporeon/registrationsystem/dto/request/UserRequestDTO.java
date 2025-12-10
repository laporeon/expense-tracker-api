package com.laporeon.registrationsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 6, max = 25, message = "Username must be {min} and {max} characters long. Please try again.")
        String username,
        @Email(message = "Invalid email")
        String email
) {
}
