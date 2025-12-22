package com.laporeon.expensetracker.dtos.response;

import java.time.LocalDateTime;

public record UpdateUserResponseDTO(
        String username,
        String email,
        LocalDateTime updatedAt
) {
}
