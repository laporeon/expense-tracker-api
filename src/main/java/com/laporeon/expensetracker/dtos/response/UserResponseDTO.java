package com.laporeon.expensetracker.dtos.response;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastAccessedAt
) {
}
