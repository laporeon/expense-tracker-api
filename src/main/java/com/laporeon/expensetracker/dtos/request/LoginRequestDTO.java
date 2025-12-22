package com.laporeon.expensetracker.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Username or email is required")
        @Schema(example = "username")
        String login,
        @NotBlank(message = "Password is required")
        @Schema(example = "#P4ssword_")
        String password
) {
}
