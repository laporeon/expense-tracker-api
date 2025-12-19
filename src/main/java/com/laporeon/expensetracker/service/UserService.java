package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.RegisterUserRequestDTO;
import com.laporeon.expensetracker.dto.request.UpdateUserRequestDTO;
import com.laporeon.expensetracker.dto.response.AuthResponseDTO;
import com.laporeon.expensetracker.dto.response.UpdateUserResponseDTO;
import com.laporeon.expensetracker.entity.User;
import com.laporeon.expensetracker.exception.AlreadyRegisteredException;
import com.laporeon.expensetracker.exception.ResourceNotFoundException;
import com.laporeon.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        .build();

        userRepository.save(user);

        return new AuthResponseDTO(
                user.getUsername(),
                user.getEmail()
        );
    }

    @Transactional
    public UpdateUserResponseDTO update(String id, UpdateUserRequestDTO dto) {
        User user = userRepository.findById(id)
                                  .filter(User::isActive)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (dto.email() != null && !dto.email().isBlank()) {
            ensureUniqueFields(user.getEmail(), dto.email());
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        userRepository.save(user);

        return new UpdateUserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.findById(id)
                      .filter(u -> u.isActive())
                      .ifPresentOrElse(
                              user -> { user.setActive(false); userRepository.save(user); },
                              () -> { throw new ResourceNotFoundException("User not found"); }
                      );
    }


    @Transactional
    public void reactivate(String id) {
        userRepository.findById(id)
                      .filter(u -> !u.isActive())
                      .ifPresentOrElse(
                              user -> { user.setActive(true); userRepository.save(user); },
                              () -> { throw new ResourceNotFoundException("User not found"); }
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
