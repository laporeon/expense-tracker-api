package com.laporeon.expensetracker.services;

import com.laporeon.expensetracker.dtos.request.UpdateUserRequestDTO;
import com.laporeon.expensetracker.dtos.response.UpdateUserResponseDTO;
import com.laporeon.expensetracker.entities.User;
import com.laporeon.expensetracker.exceptions.ResourceNotFoundException;
import com.laporeon.expensetracker.helpers.CustomValidator;
import com.laporeon.expensetracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomValidator customValidator;

    @Transactional
    public UpdateUserResponseDTO update(String id, UpdateUserRequestDTO dto) {
        User user = userRepository.findById(id)
                                  .filter(User::isActive)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (dto.email() != null && !dto.email().isBlank()) {
            customValidator.ensureUniqueFields(user.getEmail(), dto.email());
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
                      .filter(User::isActive)
                      .ifPresentOrElse(
                              user -> { user.setActive(false); userRepository.save(user); },
                              () -> { throw new ResourceNotFoundException("User not found or inactive"); }
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

}
