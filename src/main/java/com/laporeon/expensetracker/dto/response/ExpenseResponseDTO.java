package com.laporeon.expensetracker.dto.response;

import com.laporeon.expensetracker.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseResponseDTO(
        String id,
        String name,
        String description,
        BigDecimal amount,
        Category category,
        LocalDate expenseDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
