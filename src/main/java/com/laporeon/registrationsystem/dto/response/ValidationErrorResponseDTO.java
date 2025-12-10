package com.laporeon.registrationsystem.dto.response;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponseDTO(
        int status,
        String message,
        Map<String, String> details,
        Instant timestamp
){
}
