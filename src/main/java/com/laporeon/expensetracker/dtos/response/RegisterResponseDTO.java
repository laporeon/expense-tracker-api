package com.laporeon.expensetracker.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterResponseDTO(
        String token,
        String type,
        UserResponseDTO user
) {
}
