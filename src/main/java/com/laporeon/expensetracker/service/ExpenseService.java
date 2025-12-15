package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.CreateExpenseDTO;
import com.laporeon.expensetracker.dto.response.ExpenseResponseDTO;
import com.laporeon.expensetracker.dto.response.PageResponseDTO;
import com.laporeon.expensetracker.entity.Category;
import com.laporeon.expensetracker.entity.Expense;
import com.laporeon.expensetracker.exception.ResourceNotFoundException;
import com.laporeon.expensetracker.repository.CategoryRepository;
import com.laporeon.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseResponseDTO addExpense(CreateExpenseDTO dto) {
        Category category = categoryRepository.findByName(dto.category().toLowerCase())
                                              .orElseThrow(() -> new ResourceNotFoundException("Category not found. Check for available categories on: /categories"));

        Expense expense = Expense.builder()
                                 .name(dto.name())
                                 .description(dto.description())
                                 .amount(dto.amount())
                                 .categoryId(category.getId())
                                 .expenseDate(dto.expenseDate())
                                 .build();

        expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getName(),
                expense.getDescription(),
                expense.getAmount(),
                category.getName(),
                expense.getExpenseDate()
        );
    }

    public PageResponseDTO<ExpenseResponseDTO> listAllExpenses(Pageable pageable) {
        Page<ExpenseResponseDTO> expenses = expenseRepository.findAll(pageable)
                                                                 .map(expense -> new ExpenseResponseDTO(
                                                                         expense.getId(),
                                                                         expense.getName(),
                                                                         expense.getDescription(),
                                                                         expense.getAmount(),
                                                                         categoryRepository.findById(expense.getCategoryId()).get().getName(),
                                                                         expense.getExpenseDate()));

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

    public void deleteExpense(String id) {
        expenseRepository.findById(id)
                      .ifPresentOrElse(
                              expenseRepository::delete,
                              () -> {
                                  throw new ResourceNotFoundException("Expense with id '%s' not found".formatted(id));
                              });
    }
}
