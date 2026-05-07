package com.laporeon.expensetracker.dtos.response;

import com.laporeon.expensetracker.enums.Category;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal amount,
        Category category,
        LocalDate expenseDate,
        Instant createdAt,
        Instant updatedAt
) {
}
