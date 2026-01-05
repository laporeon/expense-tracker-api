package com.laporeon.expensetracker.mappers;

import com.laporeon.expensetracker.dtos.request.RegisterRequestDTO;
import com.laporeon.expensetracker.dtos.response.UserResponseDTO;
import com.laporeon.expensetracker.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(RegisterRequestDTO dto) {
        return User.builder()
                   .name(dto.name())
                   .email(dto.email())
                   .password(passwordEncoder.encode(dto.password()))
                   .lastAccessedAt(LocalDateTime.now()).build();
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastAccessedAt()
        );
    }
    
}
