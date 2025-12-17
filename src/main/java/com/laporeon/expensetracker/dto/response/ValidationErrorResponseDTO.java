package com.laporeon.expensetracker.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ValidationErrorResponseDTO(
        int status,
        String error,
        String message,
        List<Map<String, String>> errors,
        Instant timestamp
) {}

