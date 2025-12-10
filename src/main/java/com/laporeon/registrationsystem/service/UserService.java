package com.laporeon.registrationsystem.service;

import com.laporeon.registrationsystem.dto.request.UserRequestDTO;
import com.laporeon.registrationsystem.dto.response.UserResponseDTO;
import com.laporeon.registrationsystem.entity.User;
import com.laporeon.registrationsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = User.builder()
                        .username(dto.username())
                        .email(dto.email())
                        .build();

        userRepository.save(user);

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
