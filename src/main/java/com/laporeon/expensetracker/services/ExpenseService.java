package com.laporeon.expensetracker.services;

import com.laporeon.expensetracker.dtos.request.CreateExpenseRequestDTO;
import com.laporeon.expensetracker.dtos.request.UpdateExpenseRequestDTO;
import com.laporeon.expensetracker.dtos.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dtos.response.PageResponseDTO;
import com.laporeon.expensetracker.entities.Expense;
import com.laporeon.expensetracker.enums.Category;
import com.laporeon.expensetracker.exceptions.ResourceNotFoundException;
import com.laporeon.expensetracker.mappers.ExpenseMapper;
import com.laporeon.expensetracker.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;

    public ExpenseResponseDTO addExpense(CreateExpenseRequestDTO dto) {
        Expense expense = expenseMapper.toEntity(dto);

        expenseRepository.save(expense);

        return expenseMapper.toDTO(expense);
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

        return expenseMapper.toPageResponseDTO(expensesPage);
    }

    public ExpenseResponseDTO findExpense(String id) {
        Expense expense = expenseRepository.findById(id)
                                           .orElseThrow(
                                                   () -> new ResourceNotFoundException("Expense with id '%s' not found".formatted(id))
                                           );

        return expenseMapper.toDTO(expense);
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

        return expenseMapper.toDTO(expense);
    }

    private void applyUpdates(Expense expense, UpdateExpenseRequestDTO dto) {
        if (dto.name() != null) expense.setName(dto.name());
        if (dto.description() != null) expense.setDescription(dto.description());
        if (dto.amount() != null) expense.setAmount(dto.amount());
        if (dto.category() != null) expense.setCategory(Category.fromString(dto.category()));
        if (dto.expenseDate() != null) expense.setExpenseDate(dto.expenseDate());
    }

}
