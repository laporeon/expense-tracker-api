package com.laporeon.expensetracker.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email is required")
        @Schema(example = "johndoe@gmail.com")
        String email,
        @NotBlank(message = "Password is required")
        @Schema(example = "#P4ssword_")
        String password
) {
}
