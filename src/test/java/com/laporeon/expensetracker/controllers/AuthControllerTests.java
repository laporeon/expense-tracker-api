package com.laporeon.expensetracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laporeon.expensetracker.config.security.JwtAuthenticationFilter;
import com.laporeon.expensetracker.dtos.request.LoginRequestDTO;
import com.laporeon.expensetracker.dtos.request.RegisterRequestDTO;
import com.laporeon.expensetracker.dtos.response.LoginResponseDTO;
import com.laporeon.expensetracker.dtos.response.RegisterResponseDTO;
import com.laporeon.expensetracker.dtos.response.UserResponseDTO;
import com.laporeon.expensetracker.enums.Role;
import com.laporeon.expensetracker.exceptions.AlreadyRegisteredException;
import com.laporeon.expensetracker.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
@DisplayName("AuthController Tests")
class AuthControllerTests {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 201 when given valid request data")
    void shouldReturnCreatedWhenGivenValidRegisterData() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("John Doe","john@example.com","#P4ssword_");
        UserResponseDTO userDTO = new UserResponseDTO(
                UUID.randomUUID(),
                "John Doe",
                "john@example.com",
                Role.USER,
                Instant.now(),
                Instant.now(),
                Instant.now()
        );

        RegisterResponseDTO response = new RegisterResponseDTO("token", "Bearer", userDTO);

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.type").value("Bearer"))
               .andExpect(jsonPath("$.token").isNotEmpty())
               .andExpect(jsonPath("$.user.name").value(userDTO.name()))
               .andExpect(jsonPath("$.user.email").value(userDTO.email()));

        verify(authService).register(any(RegisterRequestDTO.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 400 when required fields are invalid")
    void shouldReturn400WhenRegisterRequiredFieldsAreMissing() throws Exception {
        RegisterRequestDTO invalidRequest = new RegisterRequestDTO(null, "invalid-email", "123");

        mockMvc.perform(post("/api/v1/auth/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Request validation failed for one or more fields"))
               .andExpect(jsonPath("$.errors").isArray())
               .andExpect(jsonPath("$.errors[0].field").exists());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 409 when email already registered")
    void shouldReturn409WhenEmailAlreadyRegistered() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("John 2","john@example.com","#OtherPassword123");

        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new AlreadyRegisteredException("Email already registered"));

        mockMvc.perform(post("/api/v1/auth/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.message").value("Email already registered"));

        verify(authService).register(any(RegisterRequestDTO.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 200 when given valid credentials")
    void shouldReturn200WhenGivenValidLoginCredentials() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("john@example.com", "#P4ssword_");

        UserResponseDTO userDTO = new UserResponseDTO(
                UUID.randomUUID(),
                "John Doe",
                "john@example.com",
                Role.USER,
                Instant.now(),
                Instant.now(),
                Instant.now()
        );

        LoginResponseDTO response = new LoginResponseDTO("token", "Bearer", userDTO);

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").isNotEmpty())
               .andExpect(jsonPath("$.type").value("Bearer"));

        verify(authService).login(any(LoginRequestDTO.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 400 when login fields are missing")
    void shouldReturn400WhenLoginRequiredFieldsAreMissing() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO(null, null);

        mockMvc.perform(post("/api/v1/auth/login")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Request validation failed for one or more fields"))
               .andExpect(jsonPath("$.errors").isArray());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("wrong@email.com", "senha_errada");

        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/v1/auth/login")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isUnauthorized())
               .andExpect(jsonPath("$.message").value("Invalid email or password"));

        verify(authService).login(any(LoginRequestDTO.class));
        verifyNoMoreInteractions(authService);
    }
}
