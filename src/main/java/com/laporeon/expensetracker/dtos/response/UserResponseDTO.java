package com.laporeon.expensetracker.dtos.response;

import com.laporeon.expensetracker.enums.Role;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt,
        Instant lastAccessedAt
) {
}
