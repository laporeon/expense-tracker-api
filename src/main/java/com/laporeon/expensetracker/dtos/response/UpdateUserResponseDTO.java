package com.laporeon.expensetracker.dtos.response;

import java.time.LocalDateTime;

public record UpdateUserResponseDTO(
        String name,
        String email,
        LocalDateTime updatedAt
) {
}
