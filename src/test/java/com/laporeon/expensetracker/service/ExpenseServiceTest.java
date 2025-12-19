package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.CreateExpenseRequestDTO;
import com.laporeon.expensetracker.dto.request.UpdateExpenseRequestDTO;
import com.laporeon.expensetracker.dto.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dto.response.PageResponseDTO;
import com.laporeon.expensetracker.entity.Expense;
import com.laporeon.expensetracker.enums.Category;
import com.laporeon.expensetracker.exception.ResourceNotFoundException;
import com.laporeon.expensetracker.mappers.ExpenseMapper;
import com.laporeon.expensetracker.repository.ExpenseRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpenseService Tests")
public class ExpenseServiceTest {

    private static final String VALID_NAME = "Prime Video";
    private static final String VALID_DESCRIPTION = "Prime Video annual subscription.";
    private static final BigDecimal VALID_AMOUNT = BigDecimal.valueOf(199.90);
    private static final Category VALID_CATEGORY = Category.SUBSCRIPTIONS;
    private static final LocalDate VALID_EXPENSE_DATE = LocalDate.of(2025, 12, 18);
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    @Mock
    private ExpenseRepository expenseRepository;


    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense mockedExpenseEntity;
    private ExpenseResponseDTO mockedExpenseResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        mockedExpenseEntity = Expense.builder()
                                     .id(new ObjectId().toString())
                                     .name(VALID_NAME)
                                     .description(VALID_DESCRIPTION)
                                     .amount(VALID_AMOUNT)
                                     .category(VALID_CATEGORY)
                                     .expenseDate(VALID_EXPENSE_DATE)
                                     .createdAt(createdAt)
                                     .updatedAt(updatedAt)
                                     .build();

        mockedExpenseResponse = new ExpenseResponseDTO(
                mockedExpenseEntity.getId(),
                mockedExpenseEntity.getName(),
                mockedExpenseEntity.getDescription(),
                mockedExpenseEntity.getAmount(),
                mockedExpenseEntity.getCategory(),
                mockedExpenseEntity.getExpenseDate(),
                mockedExpenseEntity.getCreatedAt(),
                mockedExpenseEntity.getUpdatedAt()
        );
    }

    @Test
    @DisplayName("Should save Expense successfully when given valid request data")
    void shouldSaveExpenseSuccessfullyWhenGivenValidRequestData() {
        CreateExpenseRequestDTO requestDTO = new CreateExpenseRequestDTO(
                VALID_NAME,
                VALID_DESCRIPTION,
                VALID_AMOUNT,
                VALID_CATEGORY.toString(),
                VALID_EXPENSE_DATE);

        when(expenseMapper.toEntity(any(CreateExpenseRequestDTO.class))).thenReturn(mockedExpenseEntity);
        when(expenseRepository.save(any(Expense.class))).thenReturn(mockedExpenseEntity);
        when(expenseMapper.toDTO(any(Expense.class))).thenReturn(mockedExpenseResponse);

        ExpenseResponseDTO response = expenseService.addExpense(requestDTO);

        assertThat(response.id()).isEqualTo(mockedExpenseResponse.id());
        assertThat(response.name()).isEqualTo(mockedExpenseResponse.name());
        assertThat(response.createdAt()).isNotNull();

        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    @DisplayName("Should return page of expenses when given valid pageable")
    void shouldReturnPageOfExpensesWhenGivenValidPageable() {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
        Page<Expense> expectedPage = new PageImpl<>(List.of(mockedExpenseEntity), pageable, 1L);

        PageResponseDTO<ExpenseResponseDTO> expectedResponse = new PageResponseDTO<>(
                List.of(mockedExpenseResponse),
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

        when(expenseRepository.findAll(pageable)).thenReturn(expectedPage);
        when(expenseMapper.toPageResponseDTO(expectedPage)).thenReturn(expectedResponse);

        PageResponseDTO<ExpenseResponseDTO> result = expenseService.listAllExpenses(pageable, null, null);

        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.content()).hasSize(1);
        verify(expenseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no expenses exist")
    void shouldReturnEmptyPageWhenNoExpensesExist() {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
        Page<Expense> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        PageResponseDTO<ExpenseResponseDTO> emptyPageResponse = new PageResponseDTO<>(
                List.of(),
                0,
                10,
                1,
                0,
                0,
                true,
                true,
                true,
                true,
                false
        );

        when(expenseRepository.findAll(pageable)).thenReturn(emptyPage);
        when(expenseMapper.toPageResponseDTO(any(Page.class))).thenReturn(emptyPageResponse);

        PageResponseDTO<ExpenseResponseDTO> result = expenseService.listAllExpenses(pageable, null, null);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.isEmpty()).isTrue();

        verify(expenseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should return expense when given existing id")
    void shouldReturnExpenseWhenGivenExistingId() {
        when(expenseRepository.findById(mockedExpenseEntity.getId())).thenReturn(Optional.of(mockedExpenseEntity));
        when(expenseMapper.toDTO(any(Expense.class))).thenReturn(mockedExpenseResponse);

        ExpenseResponseDTO sut = expenseService.findExpense(mockedExpenseEntity.getId());

        assertThat(sut.id()).isEqualTo(mockedExpenseResponse.id());
        assertThat(sut.name()).isEqualTo(mockedExpenseResponse.name());

        verify(expenseRepository, times(1)).findById(mockedExpenseEntity.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when id does not exist")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        String invalidId = "68e0234a70424186e056e45f";

        when(expenseRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.findExpense(invalidId));

        verify(expenseRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Should successfully update expense when given existing id and valid request data")
    void shouldUpdateExpenseWhenGivenExistingIdAndValidRequestData() {
        UpdateExpenseRequestDTO requestDTO = new UpdateExpenseRequestDTO(null, VALID_DESCRIPTION, null, null, null);

        when(expenseRepository.findById(mockedExpenseEntity.getId())).thenReturn(Optional.of(mockedExpenseEntity));
        when(expenseMapper.toDTO(any(Expense.class))).thenReturn(mockedExpenseResponse);
        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        ExpenseResponseDTO response = expenseService.updateExpense(mockedExpenseEntity.getId(), requestDTO);

        assertThat(response.id()).isEqualTo(mockedExpenseResponse.id());
        assertThat(response.name()).isEqualTo(mockedExpenseResponse.name());
        assertThat(response.createdAt()).isEqualTo(mockedExpenseResponse.createdAt());
        assertThat(response.updatedAt()).isNotNull();

        verify(expenseRepository, times(1)).findById(mockedExpenseEntity.getId());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating expense with non existing id")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingExpenseWithNonExistingId() {
        String invalidId = "68e0234a70424186e056e45f";
        UpdateExpenseRequestDTO requestDTO = new UpdateExpenseRequestDTO(null, VALID_DESCRIPTION, null, null, null);

        when(expenseRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.updateExpense(invalidId, requestDTO));

        verify(expenseRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Should delete expense when given existing id")
    void shouldDeleteExpenseWhenGivenExistingId() {
        when(expenseRepository.findById(mockedExpenseEntity.getId())).thenReturn(Optional.of(mockedExpenseEntity));

        doNothing().when(expenseRepository).delete(mockedExpenseEntity);

        expenseService.deleteExpense(mockedExpenseEntity.getId());

        verify(expenseRepository, times(1)).findById(mockedExpenseEntity.getId());
        verify(expenseRepository, times(1)).delete(mockedExpenseEntity);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting expense with non existing id")
    void shouldThrowResourceNotFoundExceptionWhenDeletingExpenseWithNonExistingId() {
        String invalidId = "68e0234a70424186e056e45f";

        when(expenseRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.deleteExpense(invalidId));

        verify(expenseRepository, times(1)).findById(invalidId);
        verify(expenseRepository, never()).delete(any(Expense.class));
    }
}
