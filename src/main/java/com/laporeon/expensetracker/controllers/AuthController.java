package com.laporeon.expensetracker.controllers;

import com.laporeon.expensetracker.dtos.request.LoginRequestDTO;
import com.laporeon.expensetracker.dtos.request.RegisterRequestDTO;
import com.laporeon.expensetracker.dtos.response.*;
import com.laporeon.expensetracker.helpers.SwaggerConstants;
import com.laporeon.expensetracker.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user, ensuring email and username are unique and the password meets security requirements.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUserResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.REGISTER_SUCCESS))),
                    @ApiResponse(responseCode = "400", description = "Request validation failed for one or more fields",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_INVALID_BODY_ERROR))),
                    @ApiResponse(responseCode = "409", description = "Conflict",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.CONFLICT_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        RegisterResponseDTO response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Authenticate user and return JWT token",
            description = "Validates credentials and returns access token with user details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.LOGIN_SUCCESS))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
