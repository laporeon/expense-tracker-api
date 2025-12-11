package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.RegisterUserRequestDTO;
import com.laporeon.expensetracker.dto.request.UpdateUserRequestDTO;
import com.laporeon.expensetracker.dto.response.AuthResponseDTO;
import com.laporeon.expensetracker.dto.response.UpdateUserResponseDTO;
import com.laporeon.expensetracker.entity.User;
import com.laporeon.expensetracker.exception.AlreadyRegisteredException;
import com.laporeon.expensetracker.exception.UserNotFoundException;
import com.laporeon.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthResponseDTO register(RegisterUserRequestDTO dto) {
        ensureUniqueFields(dto.username(), dto.email());

        User user = User.builder()
                        .username(dto.username())
                        .email(dto.email())
                        .password(passwordEncoder.encode(dto.password()))
                        .isActive(true)
                        .build();

        userRepository.save(user);

        return new AuthResponseDTO(
                user.getUsername(),
                user.getEmail()
        );
    }

    public UpdateUserResponseDTO update(String id, UpdateUserRequestDTO dto) {
        User user = userRepository.findById(id).filter(User::isActive).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        ensureUniqueFields(null, dto.email());

        user.setEmail(dto.email());

        userRepository.save(user);

        return new UpdateUserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getUpdatedAt()
        );
    }


    private void ensureUniqueFields(String username, String email) {
        if (username != null && !username.isEmpty() && userRepository.existsByUsername(username)) {
            throw new AlreadyRegisteredException("Username already taken");
        }

        if (email != null && !email.isEmpty() && userRepository.existsByEmail(email)) {
            throw new AlreadyRegisteredException("Email already registered");
        }
    }

}
