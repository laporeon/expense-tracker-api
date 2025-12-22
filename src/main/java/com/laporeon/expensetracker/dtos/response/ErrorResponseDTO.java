package com.laporeon.expensetracker.dtos.response;

import java.time.Instant;

public record ErrorResponseDTO(
        int status,
        String message,
        Instant timestamp
) {
}
