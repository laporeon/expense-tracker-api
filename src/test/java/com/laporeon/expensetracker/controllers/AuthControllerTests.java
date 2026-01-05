package com.laporeon.expensetracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laporeon.expensetracker.config.security.JwtAuthenticationFilter;
import com.laporeon.expensetracker.dtos.request.LoginRequestDTO;
import com.laporeon.expensetracker.dtos.request.RegisterRequestDTO;
import com.laporeon.expensetracker.dtos.response.LoginResponseDTO;
import com.laporeon.expensetracker.dtos.response.RegisterResponseDTO;
import com.laporeon.expensetracker.dtos.response.UserResponseDTO;
import com.laporeon.expensetracker.exceptions.AlreadyRegisteredException;
import com.laporeon.expensetracker.services.AuthService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
@DisplayName("AuthController Tests")
public class AuthControllerTests {

    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "johndoe@gmail.com";
    private static final String VALID_PASSWORD = "#P4ssword_";
    private static final String INVALID_REGISTER_BODY_ERROR = "Request validation failed for one or more fields";
    private static final String INVALID_CREDENTIALS_ERROR = "Invalid email or password";
    private static final String ALREADY_REGISTERED_ERROR = "Email already registered'";
    private static final String REGISTER_ENDPOINT = "/api/v1/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private String validId;
    private UserResponseDTO mockedUserResponseDTO;
    private RegisterResponseDTO mockedRegisterResponse;
    private LoginResponseDTO mockedLoginResponse;

    @BeforeEach
    void setUp() {
        validId = new ObjectId().toString();

        mockedUserResponseDTO = new UserResponseDTO(
                validId,
                VALID_NAME,
                VALID_EMAIL,
                "USER",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        mockedRegisterResponse = new RegisterResponseDTO(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                "Bearer",
                mockedUserResponseDTO
        );

        mockedLoginResponse = new LoginResponseDTO(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                "Bearer",
                mockedUserResponseDTO
        );

    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 201 when given valid request data")
    void shouldReturnCreatedWhenGivenValidRegisterData() throws Exception {
        RegisterRequestDTO validRequest = new RegisterRequestDTO(
                VALID_NAME,
                VALID_EMAIL,
                VALID_PASSWORD
        );

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(mockedRegisterResponse);

        mockMvc.perform(post(REGISTER_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.type").value("Bearer"))
               .andExpect(jsonPath("$.token").isNotEmpty())
               .andExpect(jsonPath("$.user.name").value(VALID_NAME))
               .andExpect(jsonPath("$.user.email").value(VALID_EMAIL));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 400 when required fields are invalid")
    void shouldReturn400WhenRegisterRequiredFieldsAreMissing() throws Exception {
        RegisterRequestDTO invalidRequest = new RegisterRequestDTO(
                null,
                "invalid-email",
                "123"
        );

        mockMvc.perform(post(REGISTER_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value(INVALID_REGISTER_BODY_ERROR))
               .andExpect(jsonPath("$.errors").isArray())
               .andExpect(jsonPath("$.errors[0].field").exists());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 409 when email already registered")
    void shouldReturn409WhenEmailAlreadyRegistered() throws Exception {
        RegisterRequestDTO validRequest = new RegisterRequestDTO(
                "Name",
                VALID_EMAIL,
                "#N3wP4ssw0rd_"
        );

        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new AlreadyRegisteredException(ALREADY_REGISTERED_ERROR));

        mockMvc.perform(post(REGISTER_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.message").value(ALREADY_REGISTERED_ERROR));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 200 when given valid credentials")
    void shouldReturn200WhenGivenValidLoginCredentials() throws Exception {
        LoginRequestDTO validRequest = new LoginRequestDTO(
                VALID_EMAIL,
                VALID_PASSWORD
        );

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(mockedLoginResponse);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 400 when login fields are missing")
    void shouldReturn400WhenLoginRequiredFieldsAreMissing() throws Exception {
        LoginRequestDTO invalidRequest = new LoginRequestDTO(null, null);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequestDTO invalidRequest = new LoginRequestDTO(
                "blabla@gmail.com",
                VALID_PASSWORD
        );

        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException(INVALID_CREDENTIALS_ERROR));

        mockMvc.perform(post(LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isUnauthorized())
               .andExpect(jsonPath("$.message").value(INVALID_CREDENTIALS_ERROR));
    }

}
