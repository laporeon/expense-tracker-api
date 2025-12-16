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
            summary = "Update user information",
            description = "Updates an existing user's email and/or password by ID, ensuring email remains unique and valid" +
                    "and password meets security requirements.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUserResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_UPDATE_SUCCESS))),
                    @ApiResponse(responseCode = "400", description = "Request validation failed for one or more fields",
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
                                    examples = @ExampleObject(value = SwaggerConstants.CONFLICT_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDTO> update(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequestDTO dto) {
        UpdateUserResponseDTO response = userService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Reactivates an user profile",
            description = "Reactivates an inactive user profile by ID. Only inactive users can be reactivated.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User profile successfully reactivated"),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_NOT_FOUND_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUserProfile(@PathVariable("id") String id) {
        userService.reactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Delete an user",
            description = "Deletes an existing users by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.USER_NOT_FOUND_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") String id) {
        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
