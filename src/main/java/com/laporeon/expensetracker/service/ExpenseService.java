package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.CreateExpenseRequestDTO;
import com.laporeon.expensetracker.dto.request.UpdateExpenseRequestDTO;
import com.laporeon.expensetracker.dto.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dto.response.PageResponseDTO;
import com.laporeon.expensetracker.entity.Expense;
import com.laporeon.expensetracker.enums.Category;
import com.laporeon.expensetracker.exception.ResourceNotFoundException;
import com.laporeon.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseResponseDTO addExpense(CreateExpenseRequestDTO dto) {
        Category category = Category.fromString(dto.category());

        Expense expense = Expense.builder()
                                 .name(dto.name())
                                 .description(dto.description())
                                 .amount(dto.amount())
                                 .category(category)
                                 .expenseDate(dto.expenseDate())
                                 .build();

        expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getExpenseDate()
        );
    }

    public PageResponseDTO<ExpenseResponseDTO> listAllExpenses(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Page<Expense> expensesPage;

        if (startDate != null && endDate != null) {
            expensesPage = expenseRepository.findByExpenseDateBetween(startDate, endDate, pageable);
        } else if (startDate != null) {
            expensesPage = expenseRepository.findByExpenseDateGreaterThanEqual(startDate, pageable);
        } else if (endDate != null) {
            expensesPage = expenseRepository.findByExpenseDateLessThanEqual(endDate, pageable);
        } else {
            expensesPage = expenseRepository.findAll(pageable);
        }

        return buildPageResponse(expensesPage);
    }

    public void deleteExpense(String id) {
        expenseRepository.findById(id)
                      .ifPresentOrElse(
                              expenseRepository::delete,
                              () -> {
                                  throw new ResourceNotFoundException("Expense with id '%s' not found".formatted(id));
                              });
    }

    @Transactional
    public ExpenseResponseDTO updateExpense(String id, UpdateExpenseRequestDTO dto) {
        Expense expense = expenseRepository.findById(id)
                                           .orElseThrow(() -> new ResourceNotFoundException("Expense with id '%s' not found".formatted(id)));

        applyUpdates(expense, dto);
        expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getExpenseDate()
        );
    }

    private PageResponseDTO<ExpenseResponseDTO> buildPageResponse(Page<Expense> page) {
        Page<ExpenseResponseDTO> expenses = page.map(
                e -> new ExpenseResponseDTO(
                        e.getId(),
                        e.getName(),
                        e.getDescription(),
                        e.getAmount(),
                        e.getCategory(),
                        e.getExpenseDate()));

        return new PageResponseDTO<>(
                expenses.getContent(),
                expenses.getNumber(),
                expenses.getSize(),
                expenses.getTotalPages(),
                expenses.getTotalElements(),
                expenses.getNumberOfElements(),
                expenses.isFirst(),
                expenses.isLast(),
                expenses.isEmpty(),
                expenses.getSort().isSorted(),
                expenses.getSort().isUnsorted()
        );
    }

    private void applyUpdates(Expense expense, UpdateExpenseRequestDTO dto) {
        if (dto.name() != null) expense.setName(dto.name());
        if (dto.description() != null) expense.setDescription(dto.description());
        if (dto.amount() != null) expense.setAmount(dto.amount());
        if (dto.category() != null) expense.setCategory(Category.fromString(dto.category()));
        if (dto.expenseDate() != null) expense.setExpenseDate(dto.expenseDate());
    }

}
