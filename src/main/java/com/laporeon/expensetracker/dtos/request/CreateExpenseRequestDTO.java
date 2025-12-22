package com.laporeon.expensetracker.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 25, message = "Name must be between {min}-{max} characters long")
        @Schema(example = "Prime Video")
        String name,
        @Size(min = 10, max = 50, message = "Description must be between {min}-{max} characters long")
        @Schema(example = "Amazon Prime subscription")
        String description,
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 integer digits and 2 decimal places")
        @Schema(example = "19.90")
        BigDecimal amount,
        @NotBlank(message = "Category name is required")
        @Schema(example = "subscriptions")
        String category,
        @NotNull(message = "Expense date is required (format: yyyy-MM-dd)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "2025-12-12")
        LocalDate expenseDate
) {
}
