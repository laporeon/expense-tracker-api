package com.laporeon.expensetracker.controller;

import com.laporeon.expensetracker.dto.request.UpdateUserRequestDTO;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Update user profile",
            description = "Updates a user's email and username using their unique identifier, ensuring the new email remains unique.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUserResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.UPDATE_USER_SUCCESS_RESPONSE))),
                    @ApiResponse(responseCode = "400", description = "Validation failed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_INVALID_BODY_ERROR))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_NOT_FOUND_ERROR))),
                    @ApiResponse(responseCode = "409", description = "Conflict",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.ALREADY_REGISTERED_ERROR_EXAMPLE))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDTO> update(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequestDTO dto) {
        UpdateUserResponseDTO response = userService.update(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
