package com.laporeon.expensetracker.dtos.response;

public record LoginResponseDTO(
        String token,
        String type,
        UserResponseDTO user
) {
}
