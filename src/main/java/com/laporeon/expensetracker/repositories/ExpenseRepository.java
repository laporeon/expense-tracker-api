package com.laporeon.expensetracker.repositories;

import com.laporeon.expensetracker.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {

    @Query("{'expense_date': {$gte: ?0, $lte: ?1}}")
    Page<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("{'expense_date': {$gte: ?0}}")
    Page<Expense> findByExpenseDateGreaterThanEqual(LocalDate startDate, Pageable pageable);

    @Query("{'expense_date': {$lte: ?0}}")
    Page<Expense> findByExpenseDateLessThanEqual(LocalDate endDate, Pageable pageable);

}
