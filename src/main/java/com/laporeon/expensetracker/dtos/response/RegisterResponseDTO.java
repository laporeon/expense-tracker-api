package com.laporeon.expensetracker.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterResponseDTO(
        String id,
        String name,
        String email,
        String token
) {
}
