package com.laporeon.expensetracker.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDTO(
        String id,
        String name,
        String description,
        BigDecimal amount,
        String category,
        LocalDate expenseDate
) {
}
