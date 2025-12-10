package com.laporeon.registrationsystem.dto.response;

import java.time.Instant;

public record ErrorResponseDTO(
        int status,
        String message,
        Instant timestamp
) {
}
