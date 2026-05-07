package com.laporeon.expensetracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laporeon.expensetracker.config.security.JwtAuthenticationFilter;
import com.laporeon.expensetracker.dtos.request.CreateExpenseRequestDTO;
import com.laporeon.expensetracker.dtos.request.UpdateExpenseRequestDTO;
import com.laporeon.expensetracker.dtos.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dtos.response.PageResponseDTO;
import com.laporeon.expensetracker.enums.Category;
import com.laporeon.expensetracker.exceptions.ResourceNotFoundException;
import com.laporeon.expensetracker.services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = ExpenseController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@DisplayName("ExpenseController Tests")
class ExpenseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    private ObjectMapper objectMapper;
    private UUID expenseId;
    private ExpenseResponseDTO mockedExpenseResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        expenseId = UUID.randomUUID();
        mockedExpenseResponse = new ExpenseResponseDTO(
                expenseId,
                "Prime Video",
                "Prime Video annual subscription.",
                BigDecimal.valueOf(199.90),
                Category.SUBSCRIPTIONS,
                LocalDate.of(2025, 12, 18),
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    @DisplayName("POST /api/v1/expenses - Should return 201 when given valid request data")
    void shouldReturnCreatedWhenGivenValidRequestData() throws Exception {
        CreateExpenseRequestDTO request = new CreateExpenseRequestDTO(
                "Prime Video",
                "Prime Video annual subscription.",
                BigDecimal.valueOf(199.90),
                "SUBSCRIPTIONS",
                LocalDate.of(2025, 12, 18)
        );

        when(expenseService.addExpense(any(CreateExpenseRequestDTO.class))).thenReturn(mockedExpenseResponse);

        mockMvc.perform(post("/api/v1/expenses")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(expenseId.toString()))
               .andExpect(jsonPath("$.name").value(request.name()));

        verify(expenseService).addExpense(any(CreateExpenseRequestDTO.class));
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("POST /api/v1/expenses - Should return 400 when required fields are missing")
    void shouldReturn400WhenRequiredFieldsAreMissing() throws Exception {
        CreateExpenseRequestDTO request = new CreateExpenseRequestDTO(null, null, null, null, null);

        mockMvc.perform(post("/api/v1/expenses")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Request validation failed for one or more fields"))
               .andExpect(jsonPath("$.errors").isArray());

        verifyNoInteractions(expenseService);
    }

    @Test
    @DisplayName("POST /api/v1/expenses - Should return 422 when given invalid category value")
    void shouldReturn422WhenGivenInvalidCategoryValue() throws Exception {
        CreateExpenseRequestDTO request = new CreateExpenseRequestDTO("Prime Video", null, BigDecimal.valueOf(10), "INVALID", LocalDate.of(2024, 12, 10));

        when(expenseService.addExpense(any(CreateExpenseRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid category name 'INVALID'"));

        mockMvc.perform(post("/api/v1/expenses")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(jsonPath("$.message").value("Invalid category name 'INVALID'"));

        verify(expenseService).addExpense(any(CreateExpenseRequestDTO.class));
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("GET /api/v1/expenses - Should return 200 and paginated expenses with empty startDate and endDate")
    void shouldReturn200AndPaginatedExpenses() throws Exception {
        List<ExpenseResponseDTO> content = List.of(mockedExpenseResponse);
        PageResponseDTO<ExpenseResponseDTO> pageResponse = new PageResponseDTO<>(
                content,
                0,
                10,
                1,
                1,
                1,
                true,
                true,
                false,
                true,
                false
        );

        when(expenseService.listAllExpenses(any(), any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/expenses")
                       .param("page", "0")
                       .param("size", "10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content").isArray())
               .andExpect(jsonPath("$.content").isNotEmpty())
               .andExpect(jsonPath("$.totalElements").value(content.size()));

        verify(expenseService).listAllExpenses(any(), any(), any());
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("GET /api/v1/expenses - Should return 200 with empty content when no expenses exist")
    void shouldReturn200WithEmptyContentWhenNoExpensesExist() throws Exception {
        PageResponseDTO<ExpenseResponseDTO> pageResponse = new PageResponseDTO<>(
                List.of(),
                0,
                10,
                0,
                0,
                0,
                true,
                true,
                true,
                true,
                false
        );

        when(expenseService.listAllExpenses(any(), any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/expenses"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content").isEmpty())
               .andExpect(jsonPath("$.totalElements").value(pageResponse.content().size()));

        verify(expenseService).listAllExpenses(any(), any(), any());
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("GET /api/v1/expenses/{id} - Should return 200 when id exists")
    void shouldReturn200WhenIdExists() throws Exception {
        when(expenseService.findExpense(expenseId)).thenReturn(mockedExpenseResponse);

        mockMvc.perform(get("/api/v1/expenses/" + expenseId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(expenseId.toString()))
               .andExpect(jsonPath("$.name").value(mockedExpenseResponse.name()));

        verify(expenseService).findExpense(expenseId);
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("GET /api/v1/expenses/{id} - Should return 404 when id does not exist")
    void shouldReturn404WhenIdDoesNotExist() throws Exception {
        UUID invalidId = UUID.randomUUID();

        when(expenseService.findExpense(invalidId)).thenThrow(new ResourceNotFoundException("Expense with id " + invalidId + " not found."));

        mockMvc.perform(get("/api/v1/expenses/" + invalidId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());

        verify(expenseService).findExpense(invalidId);
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("PATCH /api/v1/expenses/{id} - Should return 200 when updating expense with existing id and valid request data")
    void shouldReturn200WhenUpdatingExpenseWithExistingIdAndValidRequestData() throws Exception {
        UpdateExpenseRequestDTO request = new UpdateExpenseRequestDTO(null, "New Subscription Description", null, null, null);

        ExpenseResponseDTO updatedResponse = new ExpenseResponseDTO(
                expenseId,
                "Prime Video",
                "New Subscription Description",   // <-- reflete o update
                BigDecimal.valueOf(199.90),
                Category.SUBSCRIPTIONS,
                LocalDate.of(2025, 12, 18),
                Instant.now(),
                Instant.now()
        );

        when(expenseService.updateExpense(eq(expenseId), any(UpdateExpenseRequestDTO.class))).thenReturn(updatedResponse);

        mockMvc.perform(patch("/api/v1/expenses/" + expenseId)
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(expenseId.toString()))
               .andExpect(jsonPath("$.description").value(request.description()));

        verify(expenseService).updateExpense(eq(expenseId), any(UpdateExpenseRequestDTO.class));
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("PATCH /api/v1/expenses/{id} - Should return 404 when updating expense with non existing id")
    void shouldReturn404WhenUpdatingExpenseWithNonExistingId() throws Exception {
        UUID invalidId = UUID.randomUUID();
        UpdateExpenseRequestDTO request = new UpdateExpenseRequestDTO(null, null, BigDecimal.valueOf(29.90), null, null);
        doThrow(new ResourceNotFoundException("Expense with id " + invalidId + " not found."))
                .when(expenseService)
                .updateExpense(invalidId, request);

        mockMvc.perform(patch("/api/v1/expenses/" + invalidId)
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());

        verify(expenseService).updateExpense(eq(invalidId), any(UpdateExpenseRequestDTO.class));
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("DELETE /api/v1/expenses/{id} - Should return 204 when deleting expense with existing id")
    void shouldReturn204WhenDeletingExpenseWithExistingId() throws Exception {
        doNothing().when(expenseService).deleteExpense(expenseId);

        mockMvc.perform(delete("/api/v1/expenses/" + expenseId))
               .andExpect(status().isNoContent());

        verify(expenseService).deleteExpense(expenseId);
        verifyNoMoreInteractions(expenseService);
    }

    @Test
    @DisplayName("DELETE /api/v1/expenses/{id} - Should return 404 when deleting expense with non existing id")
    void shouldReturn404WhenDeletingExpenseWithNonExistingId() throws Exception {
        UUID invalidId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("Expense with id " + invalidId + " not found."))
                .when(expenseService)
                .deleteExpense(invalidId);

        mockMvc.perform(delete("/api/v1/expenses/" + invalidId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());

        verify(expenseService).deleteExpense(invalidId);
        verifyNoMoreInteractions(expenseService);
    }
}
