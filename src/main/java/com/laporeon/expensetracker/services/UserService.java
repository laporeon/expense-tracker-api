package com.laporeon.expensetracker.services;

import com.laporeon.expensetracker.dtos.request.UpdateUserRequestDTO;
import com.laporeon.expensetracker.dtos.response.UserResponseDTO;
import com.laporeon.expensetracker.entities.User;
import com.laporeon.expensetracker.exceptions.AlreadyRegisteredException;
import com.laporeon.expensetracker.exceptions.ResourceNotFoundException;
import com.laporeon.expensetracker.mappers.UserMapper;
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
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO update(String id, UpdateUserRequestDTO dto) {
        User user = userRepository.findByIdAndActiveTrue(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found or inactive"));

        if (userRepository.existsByEmail(dto.email())) {
            throw new AlreadyRegisteredException("Email already registered");
        }

        applyUpdates(user, dto);

        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found or inactive"));

        user.setActive(false);
        userRepository.save(user);
    }


    private void applyUpdates(User user, UpdateUserRequestDTO dto) {
        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.password() != null)  user.setPassword(passwordEncoder.encode(dto.password()));
    }

}
