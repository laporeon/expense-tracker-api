package com.laporeon.expensetracker.services;

import com.laporeon.expensetracker.dtos.request.LoginRequestDTO;
import com.laporeon.expensetracker.dtos.request.RegisterRequestDTO;
import com.laporeon.expensetracker.dtos.response.LoginResponseDTO;
import com.laporeon.expensetracker.dtos.response.RegisterResponseDTO;
import com.laporeon.expensetracker.entities.User;
import com.laporeon.expensetracker.helpers.CustomValidator;
import com.laporeon.expensetracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomValidator customValidator;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        customValidator.ensureUniqueFields(dto.username(), dto.email());

        User user = User.builder()
                        .name(dto.name())
                        .username(dto.username())
                        .email(dto.email())
                        .password(passwordEncoder.encode(dto.password()))
                        .build();

        userRepository.save(user);

        String token = tokenService.generateToken(user);

        return new RegisterResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                token
        );
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        var loginPassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        User user = (User) authenticationManager.authenticate(loginPassword).getPrincipal();

        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token);
    }

}
