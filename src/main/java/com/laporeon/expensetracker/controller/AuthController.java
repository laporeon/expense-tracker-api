package com.laporeon.expensetracker.controller;

import com.laporeon.expensetracker.dto.request.RegisterUserRequestDTO;
import com.laporeon.expensetracker.dto.response.AuthResponseDTO;
import com.laporeon.expensetracker.dto.response.ErrorResponseDTO;
import com.laporeon.expensetracker.dto.response.UpdateUserResponseDTO;
import com.laporeon.expensetracker.dto.response.ValidationErrorResponseDTO;
import com.laporeon.expensetracker.helpers.SwaggerConstants;
import com.laporeon.expensetracker.service.UserService;
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
@RequestMapping("/api/v1")
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user, ensuring email and username are unique and the password meets security requirements.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUserResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.AUTH_RESPONSE_EXAMPLE))),
                    @ApiResponse(responseCode = "400", description = "Validation failed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.VALIDATION_ERROR_EXAMPLE))),
                    @ApiResponse(responseCode = "409", description = "Conflict",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.ALREADY_REGISTERED_ERROR_EXAMPLE))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> registerUser(@Valid @RequestBody RegisterUserRequestDTO dto) {
        AuthResponseDTO response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
