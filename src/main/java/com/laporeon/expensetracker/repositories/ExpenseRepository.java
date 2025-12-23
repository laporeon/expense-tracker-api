package com.laporeon.expensetracker.repositories;

import com.laporeon.expensetracker.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {

    @Query("{ 'userId': ?0, 'date': { $gte: ?1, $lte: ?2 } }")
    Page<Expense> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate,Pageable pageable);

    @Query("{ 'userId': ?0, 'date': { $gte: ?1 } }")
    Page<Expense> findByUserIdAndDateGreaterThanEqual(String userId, LocalDate startDate, Pageable pageable);

    @Query("{ 'userId': ?0, 'date': { $lte: ?1 } }")
    Page<Expense> findByUserIdAndDateLessThanEqual(String userId, LocalDate endDate, Pageable pageable);

    Page<Expense> findAllByUserId(String userId, Pageable pageable);

    Optional<Expense> findByIdAndUserId(String id, String userId);

}
