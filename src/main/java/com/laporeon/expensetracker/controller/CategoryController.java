package com.laporeon.expensetracker.controller;

import com.laporeon.expensetracker.dto.request.CreateCategoryDTO;
import com.laporeon.expensetracker.dto.response.*;
import com.laporeon.expensetracker.helpers.SwaggerConstants;
import com.laporeon.expensetracker.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Creates a new spending category ensuring that the category name is unique for the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.CATEGORY_CREATED_RESPONSE))),
                    @ApiResponse(responseCode = "400", description = "Validation failed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.CATEGORY_INVALID_BODY_ERROR))),
                    @ApiResponse(responseCode = "409", description = "Category name already registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.CATEGORY_ALREADY_REGISTERED_ERROR))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @PostMapping()
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CreateCategoryDTO dto) {
        CategoryResponseDTO response = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all categories",
            description = "Returns a paginated list of categories sorted by name in ASC order, allowing control over page number and size.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories successfully listed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.CATEGORIES_PAGE_RESPONSE))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDTO.class),
                                    examples = @ExampleObject(value = SwaggerConstants.GENERIC_ERROR_EXAMPLE))),
            })
    @GetMapping()
    public ResponseEntity<PageResponseDTO> listCategories(
            @Parameter(description = "Page number")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(value = "size", defaultValue = "10") int size
) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        PageResponseDTO<CategoryResponseDTO> response = categoryService.listCategories(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
