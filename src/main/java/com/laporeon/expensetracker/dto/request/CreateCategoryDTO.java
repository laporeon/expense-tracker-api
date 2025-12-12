package com.laporeon.expensetracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryDTO(
        @NotBlank(message = "Category name is required")
        @Size(min = 3, max = 15, message = "Category name must be {min}-{max} characters long")
        @Schema(example = "food")
        String name
) {
}
