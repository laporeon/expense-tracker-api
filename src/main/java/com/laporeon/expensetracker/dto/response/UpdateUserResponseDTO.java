package com.laporeon.expensetracker.dto.response;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String username,
        String email,
        LocalDateTime updatedAt
) {
}
