package com.laporeon.registrationsystem.dto.response;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String id,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
