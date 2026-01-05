package com.laporeon.expensetracker.services;

import com.laporeon.expensetracker.dtos.request.LoginRequestDTO;
import com.laporeon.expensetracker.dtos.request.RegisterRequestDTO;
import com.laporeon.expensetracker.dtos.response.LoginResponseDTO;
import com.laporeon.expensetracker.dtos.response.RegisterResponseDTO;
import com.laporeon.expensetracker.dtos.response.UserResponseDTO;
import com.laporeon.expensetracker.entities.User;
import com.laporeon.expensetracker.exceptions.AlreadyRegisteredException;
import com.laporeon.expensetracker.helpers.JwtTokenProvider;
import com.laporeon.expensetracker.helpers.SecurityUtils;
import com.laporeon.expensetracker.mappers.UserMapper;
import com.laporeon.expensetracker.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
public class AuthServiceTest {

    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "johndoe@gmail.com";
    private static final String VALID_PASSWORD = "#P4ssword_";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authenticationResult;

    @InjectMocks
    private AuthService authService;

    private User mockedUserEntity;
    private UserResponseDTO mockedUserResponseDTO;
    private String userId;
    private MockedStatic<SecurityUtils> mockedSecurity;

    @BeforeEach
    void setUp() {
        userId = "507f1f77bcf86cd799439011";

        mockedUserEntity = User.builder()
                               .id(userId)
                               .name(VALID_NAME)
                               .email(VALID_EMAIL)
                               .password("$2a$10$encodedPasswordHash")
                               .createdAt(LocalDateTime.now())
                               .updatedAt(LocalDateTime.now())
                               .build();

        mockedUserResponseDTO = new UserResponseDTO(
                userId,
                VALID_NAME,
                VALID_EMAIL,
                "USER",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        mockedSecurity = mockStatic(SecurityUtils.class);
        mockedSecurity.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
    }

    @AfterEach
    void tearDown() {
        mockedSecurity.close();
    }

    @Test
    @DisplayName("Should register User successfully when given valid request data")
    void shouldRegisterUserSuccessfullyWhenGivenValidRequestData() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                VALID_NAME,
                VALID_EMAIL,
                VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(userMapper.toEntity(requestDTO)).thenReturn(mockedUserEntity);
        when(userRepository.save(any(User.class))).thenReturn(mockedUserEntity);
        when(jwtTokenProvider.generateToken(any(User.class))).thenReturn(VALID_TOKEN);
        when(userMapper.toResponseDTO(mockedUserEntity)).thenReturn(mockedUserResponseDTO);

        RegisterResponseDTO response = authService.register(requestDTO);

        assertThat(response.token()).isNotNull();

        verify(userRepository).existsByEmail(VALID_EMAIL);
        verify(userMapper).toEntity(requestDTO);
        verify(userRepository).save(any(User.class));
        verify(jwtTokenProvider).generateToken(any(User.class));
        verify(userMapper).toResponseDTO(mockedUserEntity);

    }

    @Test
    @DisplayName("Should throw AlreadyRegisteredException when email already exists")
    void shouldThrowAlreadyRegisteredExceptionWhenEmailAlreadyExists() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                VALID_NAME,
                VALID_EMAIL,
                VALID_PASSWORD);

        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(true);
        assertThrows(AlreadyRegisteredException.class, () -> authService.register(requestDTO));

        verify(userRepository).existsByEmail(VALID_EMAIL);
        verify(userRepository, never()).save(any());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should return token and update lastAccessedAt when login credentials are valid")
    void shouldReturnTokenAndUpdateLastAccessedAtWhenLoginCredentialsAreValid() {
        LoginRequestDTO request = new LoginRequestDTO(VALID_EMAIL, VALID_PASSWORD);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(VALID_EMAIL, VALID_PASSWORD);

        when(authenticationManager.authenticate(authToken)).thenReturn(authenticationResult);
        when(authenticationResult.getPrincipal()).thenReturn(mockedUserEntity);
        when(jwtTokenProvider.generateToken(mockedUserEntity)).thenReturn(VALID_TOKEN);
        when(userRepository.save(any(User.class))).thenReturn(mockedUserEntity);
        when(userMapper.toResponseDTO(mockedUserEntity)).thenReturn(mockedUserResponseDTO);

        LoginResponseDTO response = authService.login(request);

        assertThat(response.token()).isEqualTo(VALID_TOKEN);
        assertThat(response.type()).isEqualTo("Bearer");

        verify(authenticationManager).authenticate(authToken);
        verify(userRepository).save(argThat(user ->
                                                    user.getLastAccessedAt() != null
        ));
        verify(jwtTokenProvider).generateToken(mockedUserEntity);
        verify(userMapper).toResponseDTO(mockedUserEntity);
    }

}
