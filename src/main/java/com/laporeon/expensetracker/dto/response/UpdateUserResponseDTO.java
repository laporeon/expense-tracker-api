package com.laporeon.expensetracker.dto.response;

import java.time.LocalDateTime;

public record UpdateUserResponseDTO(
        String username,
        String email,
        LocalDateTime updatedAt
) {
}
