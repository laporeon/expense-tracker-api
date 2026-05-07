package com.laporeon.expensetracker.entities;

import com.laporeon.expensetracker.dtos.request.UpdateExpenseRequestDTO;
import com.laporeon.expensetracker.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 150)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Category category;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Expense create(String name, String description, BigDecimal amount, Category category, UUID userId, LocalDate date) {
        return Expense.builder()
                      .name(name)
                      .description(description)
                      .amount(amount)
                      .category(category)
                      .userId(userId)
                      .date(date)
                      .createdAt(Instant.now())
                      .updatedAt(Instant.now())
                      .build();
    }

    public void update(UpdateExpenseRequestDTO dto) {
        if (dto.name() != null)        this.name = dto.name();
        if (dto.description() != null) this.description = dto.description();
        if (dto.amount() != null)      this.amount = dto.amount();
        if (dto.category() != null)    this.category = Category.fromString(dto.category());
        if (dto.date() != null)        this.date = dto.date();
        this.updatedAt = Instant.now();
    }

}