package com.laporeon.expensetracker.controller;

import com.laporeon.expensetracker.dto.request.CreateExpenseDTO;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expenses")
@Tag(name = "Expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(
            summary = "Create a new expense",
            description = "Creates a new expense entry associated with a valid category. " +
                    "The expense amount must be greater than zero and the category must exist.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Expense successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_CREATED_RESPONSE))),
                    @ApiResponse(responseCode = "400", description = "Validation failed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSE_INVALID_BODY_ERROR))),
                    @ApiResponse(responseCode = "422", description = "Invalid category",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.INVALID_EXPENSE_CATEGORY_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @PostMapping()
    public ResponseEntity<ExpenseResponseDTO> createExpense(@Valid @RequestBody CreateExpenseDTO dto) {
        ExpenseResponseDTO response = expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all expenses",
            description = "Retrieves a paginated and sorted list of expenses, allowing the client to control page number, page size, sort field and sort direction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expenses page successfully retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.EXPENSES_PAGE_RESPONSE))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @GetMapping()
    public ResponseEntity<PageResponseDTO> listAllExpenses(
            @Parameter(description = "Page number")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Entity field used for sorting (e.g. name, amount, date)")
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @Parameter(description = "Sort direction (ASC or DESC)")
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size,
                                           Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), orderBy));

        PageResponseDTO<ExpenseResponseDTO> expenses = expenseService.listAllExpenses(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(expenses);
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
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") String id) {
        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
