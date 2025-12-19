package com.laporeon.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laporeon.expensetracker.config.SecurityConfiguration;
import com.laporeon.expensetracker.dto.request.CreateExpenseRequestDTO;
import com.laporeon.expensetracker.dto.request.UpdateExpenseRequestDTO;
import com.laporeon.expensetracker.dto.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dto.response.PageResponseDTO;
import com.laporeon.expensetracker.enums.Category;
import com.laporeon.expensetracker.exception.ResourceNotFoundException;
import com.laporeon.expensetracker.service.ExpenseService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
@Import(SecurityConfiguration.class)
@DisplayName("ExpenseController Tests")
class ExpenseControllerTests {

    private static final String VALID_NAME = "Prime Video";
    private static final String VALID_DESCRIPTION = "Prime Video annual subscription.";
    private static final BigDecimal VALID_AMOUNT = BigDecimal.valueOf(199.90);
    private static final Category VALID_CATEGORY = Category.SUBSCRIPTIONS;
    private static final LocalDate VALID_EXPENSE_DATE = LocalDate.of(2025, 12, 18);
    private static final String INVALID_REQUEST_BODY_ERROR = "Request validation failed for one or more fields";
    private static final String INVALID_CATEGORY_VALUE_ERROR = "Invalid category name '%s'";
    private static final String NOT_FOUND_MESSAGE = "Expense with id %s not found.";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final String EXPENSES_ENDPOINT = "/api/v1/expenses";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExpenseService expenseService;

    private ExpenseResponseDTO mockedExpenseResponse;
    private String validExpenseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        validExpenseId = new ObjectId().toString();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        mockedExpenseResponse = new ExpenseResponseDTO(
                validExpenseId,
                VALID_NAME,
                VALID_DESCRIPTION,
                VALID_AMOUNT,
                VALID_CATEGORY,
                VALID_EXPENSE_DATE,
                createdAt,
                updatedAt
        );
    }

    @Test
    @DisplayName("POST /api/v1/expenses - Should return 201 when given valid request data")
    void shouldReturnCreatedWhenGivenValidRequestData() throws Exception {
        CreateExpenseRequestDTO validRequest =
                new CreateExpenseRequestDTO(
                        VALID_NAME,
                        VALID_DESCRIPTION,
                        VALID_AMOUNT,
                        VALID_CATEGORY.toString(),
                        VALID_EXPENSE_DATE
                );

        when(expenseService.addExpense(any(CreateExpenseRequestDTO.class))).thenReturn(mockedExpenseResponse);

        mockMvc.perform(post(EXPENSES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(mockedExpenseResponse.id()))
               .andExpect(jsonPath("$.name").value(mockedExpenseResponse.name()));
    }

    @Test
    @DisplayName("POST /api/v1/expenses - Should return 400 when required fields are missing")
    void shouldReturn400WhenRequiredFieldsAreMissing() throws Exception {
        CreateExpenseRequestDTO invalidRequest = new CreateExpenseRequestDTO(null,
                                                                             VALID_DESCRIPTION,
                                                                             null,
                                                                             null,
                                                                             null);

        mockMvc.perform(post(EXPENSES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value(INVALID_REQUEST_BODY_ERROR))
               .andExpect(jsonPath("$.errors").isArray())
               .andExpect(jsonPath("$.errors[0].field").value("amount"))
               .andExpect(jsonPath("$.errors[0].message").value("Amount is required"))
               .andExpect(jsonPath("$.errors[1].field").value("category"))
               .andExpect(jsonPath("$.errors[1].message").value("Category name is required"))
               .andExpect(jsonPath("$.errors[2].field").value("expenseDate"))
               .andExpect(jsonPath("$.errors[2].message").value("Expense date is required (format: yyyy-MM-dd)"))
               .andExpect(jsonPath("$.errors[3].field").value("name"))
               .andExpect(jsonPath("$.errors[3].message").value("Name is required"));
    }

    @Test
    @DisplayName("POST /api/v1/expenses - Should return 422 when given invalid category value")
    void shouldReturn422WhenGivenInvalidCategoryValue() throws Exception {
        String invalidCategoryValue = "invalidCategoryValue";
        CreateExpenseRequestDTO invalidRequest = new CreateExpenseRequestDTO(VALID_NAME,
                                                                             VALID_DESCRIPTION,
                                                                             VALID_AMOUNT,
                                                                             invalidCategoryValue,
                                                                             VALID_EXPENSE_DATE);


        when(expenseService.addExpense(any(CreateExpenseRequestDTO.class)))
                .thenThrow(new IllegalArgumentException(INVALID_CATEGORY_VALUE_ERROR.formatted(invalidCategoryValue)));

        mockMvc.perform(post(EXPENSES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(jsonPath("$.message").value(INVALID_CATEGORY_VALUE_ERROR.formatted(invalidCategoryValue)));
    }

    @Test
    @DisplayName("GET /api/v1/expenses - Should return 200 and paginated expenses with empty startDate and endDate")
    void shouldReturn200AndPaginatedExpenses() throws Exception {
        List<ExpenseResponseDTO> content = List.of(mockedExpenseResponse);
        PageResponseDTO<ExpenseResponseDTO> pageResponse = new PageResponseDTO<>(
                content,
                DEFAULT_PAGE,
                DEFAULT_SIZE,
                1,
                1,
                1,
                true,
                true,
                false,
                true,
                false
        );

        when(expenseService.listAllExpenses(any(Pageable.class), any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get(EXPENSES_ENDPOINT)
                                .param("page", String.valueOf(DEFAULT_PAGE))
                                .param("size", String.valueOf(DEFAULT_SIZE)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content").isArray())
               .andExpect(jsonPath("$.content").isNotEmpty())
               .andExpect(jsonPath("$.totalElements").value(1))
               .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE))
               .andExpect(jsonPath("$.pageSize").value(DEFAULT_SIZE));
    }

    @Test
    @DisplayName("GET /api/v1/expenses - Should return 200 with empty content when no expenses exist")
    void shouldReturn200WithEmptyContentWhenNoExpensesExist() throws Exception {
        PageResponseDTO<ExpenseResponseDTO> pageResponse = new PageResponseDTO<>(
                List.of(),
                DEFAULT_PAGE,
                DEFAULT_SIZE,
                0,
                0,
                0,
                true,
                true,
                true,
                true,
                false
        );

        when(expenseService.listAllExpenses(any(Pageable.class), any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get(EXPENSES_ENDPOINT))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content").isEmpty())
               .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/expenses/{id} - Should return 200 when id exists")
    void shouldReturn200WhenIdExists() throws Exception {
        when(expenseService.findExpense(validExpenseId)).thenReturn(mockedExpenseResponse);

        mockMvc.perform(get(EXPENSES_ENDPOINT + "/" + validExpenseId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(mockedExpenseResponse.id()))
               .andExpect(jsonPath("$.name").value(mockedExpenseResponse.name()));
    }

    @Test
    @DisplayName("GET /api/v1/expenses/{id} - Should return 404 when id does not exist")
    void shouldReturn404WhenIdDoesNotExist() throws Exception {
        String invalidId = "68e0124a70424186e056e45d";

        when(expenseService.findExpense(invalidId)).thenThrow(new ResourceNotFoundException(NOT_FOUND_MESSAGE.formatted(invalidId)));

        mockMvc.perform(get(EXPENSES_ENDPOINT + "/" + invalidId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(NOT_FOUND_MESSAGE.formatted(invalidId)));
    }

    @Test
    @DisplayName("PATCH /api/v1/expenses/{id} - Should return 200 when updating expense with existing id and valid request data")
    void shouldReturn200WhenUpdatingExpenseWithExistingIdAndValidRequestData() throws Exception {
        UpdateExpenseRequestDTO validRequest = new UpdateExpenseRequestDTO(null, VALID_DESCRIPTION, null, null, null);

        when(expenseService.updateExpense(eq(validExpenseId), any(UpdateExpenseRequestDTO.class)))
                .thenReturn(mockedExpenseResponse);

        mockMvc.perform(patch(EXPENSES_ENDPOINT + "/" + validExpenseId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(mockedExpenseResponse.id()))
               .andExpect(jsonPath("$.name").value(mockedExpenseResponse.name()))
               .andExpect(jsonPath("$.description").value(mockedExpenseResponse.description()))
               .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    @DisplayName("PATCH /api/v1/expenses/{id} - Should return 404 when updating expense with non existing id")
    void shouldReturn404WhenGivenNonExistingId() throws Exception {
        String invalidId = "68e0124a70424186e056e45d";
        UpdateExpenseRequestDTO validRequest = new UpdateExpenseRequestDTO(null, VALID_DESCRIPTION, null, null, null);

        doThrow(new ResourceNotFoundException(NOT_FOUND_MESSAGE.formatted(invalidId)))
                .when(expenseService)
                .updateExpense(invalidId, validRequest);

        mockMvc.perform(patch(EXPENSES_ENDPOINT + "/" + invalidId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(NOT_FOUND_MESSAGE.formatted(invalidId)));
    }

    @Test
    @DisplayName("DELETE /api/v1/expenses/{id} - Should return 204 when deleting expense with existing id")
    void shouldReturn204WhenDeletingExpenseWithExistingId() throws Exception {
        doNothing().when(expenseService).deleteExpense(validExpenseId);

        mockMvc.perform(delete(EXPENSES_ENDPOINT + "/" + validExpenseId))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/expenses/{id} - Should return 404 when deleting expense with non existing id")
    void shouldReturn404WhenDeletingExpenseWithNonExistingId() throws Exception {
        String invalidId = "68e0124a70424186e056e45d";

        doThrow(new ResourceNotFoundException(NOT_FOUND_MESSAGE.formatted(invalidId)))
                .when(expenseService)
                .deleteExpense(invalidId);

        mockMvc.perform(delete(EXPENSES_ENDPOINT + "/" + invalidId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(NOT_FOUND_MESSAGE.formatted(invalidId)));
    }
}
