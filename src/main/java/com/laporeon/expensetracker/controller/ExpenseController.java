package com.laporeon.expensetracker.controller;

import com.laporeon.expensetracker.dto.request.CreateExpenseDTO;
import com.laporeon.expensetracker.dto.request.UpdateExpenseDTO;
import com.laporeon.expensetracker.dto.response.ErrorResponseDTO;
import com.laporeon.expensetracker.dto.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dto.response.PageResponseDTO;
import com.laporeon.expensetracker.dto.response.ValidationErrorResponseDTO;
import com.laporeon.expensetracker.helpers.SwaggerConstants;
import com.laporeon.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expenses")
@Tag(name = "Expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(
            summary = "Create a new expense",
            description = "Creates a new expense entry. The amount must be greater than zero and the category must be a valid enum value.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Expense successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_CREATE_SUCCESS))),
                    @ApiResponse(responseCode = "400", description = "Request validation failed for one or more fields",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_INVALID_BODY_ERROR))),
                    @ApiResponse(responseCode = "422", description = "Invalid category value",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.INVALID_CATEGORY_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @PostMapping()
    public ResponseEntity<ExpenseResponseDTO> createExpense(@Valid @RequestBody CreateExpenseDTO dto) {
        ExpenseResponseDTO response = expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List expenses",
            description = "Retrieves a paginated and sorted list of expenses with optional inclusive date range filtering.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expenses page successfully retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSES_PAGE_SUCCESS))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @GetMapping()
    public ResponseEntity<PageResponseDTO> listAllExpenses(
            @Parameter(description = "Page number")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Entity field used for sorting",
                    schema = @Schema(allowableValues = {"name", "amount", "expenseDate", "category"}),
                    example = "name")
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @Parameter(description = "Sort direction",
                    schema = @Schema(allowableValues = {"ASC", "DESC"}),
                    example = "ASC")
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @Parameter(description = "Filter expenses from this date (format: yyyy-MM-dd)")
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @Parameter(description = "Filter expenses until this date (format: yyyy-MM-dd)")
            @RequestParam(value = "endDate", required = false) LocalDate endDate
    ) {
        Pageable pageable = PageRequest.of(page, size,
                                           Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), orderBy));

        PageResponseDTO<ExpenseResponseDTO> expenses = expenseService.listAllExpenses(pageable, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(expenses);
    }

    @Operation(
            summary = "Partially update an expense",
            description = "Partially updates an existing expense by its ID. At least one field must be provided in the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expense successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_UPDATE_SUCCESS))),
                    @ApiResponse(responseCode = "400", description = "Request validation failed for one or more fields",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_INVALID_BODY_ERROR))),
                    @ApiResponse(responseCode = "404", description = "Expense not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_NOT_FOUND_ERROR))),
                    @ApiResponse(responseCode = "422", description = "Invalid category value",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.INVALID_CATEGORY_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable("id") String id, @Valid @RequestBody UpdateExpenseDTO dto) {
        ExpenseResponseDTO response = expenseService.updateExpense(id, dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Delete an expense",
            description = "Deletes an existing expense by its ID. Returns 204 when the expense is successfully deleted.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Expense successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Expense not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_NOT_FOUND_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.SERVER_ERROR))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") String id) {
        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
