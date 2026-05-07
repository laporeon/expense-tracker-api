package com.laporeon.expensetracker.repositories;

import com.laporeon.expensetracker.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    Page<Expense> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate,Pageable pageable);

    Page<Expense> findByUserIdAndDateGreaterThanEqual(UUID userId, LocalDate startDate, Pageable pageable);

    Page<Expense> findByUserIdAndDateLessThanEqual(UUID userId, LocalDate endDate, Pageable pageable);

    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);

}
