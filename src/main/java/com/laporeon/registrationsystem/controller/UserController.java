package com.laporeon.registrationsystem.controller;

import com.laporeon.registrationsystem.dto.request.UserRequestDTO;
import com.laporeon.registrationsystem.dto.response.ErrorResponseDTO;
import com.laporeon.registrationsystem.dto.response.UserResponseDTO;
import com.laporeon.registrationsystem.dto.response.ValidationErrorResponseDTO;
import com.laporeon.registrationsystem.helpers.SwaggerConstants;
import com.laporeon.registrationsystem.service.UserService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Register a new user, ensuring email and username are unique.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_RESPONSE_EXAMPLE))),
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
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
