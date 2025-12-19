package com.laporeon.expensetracker.entity;

import com.laporeon.expensetracker.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Document(collection = "expenses")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @MongoId
    private String id;

    private String name;

    private String description;

    @Field(targetType = DECIMAL128)
    private BigDecimal amount;

    private Category category;

    @Field(name = "expense_date")
    private LocalDate expenseDate;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private LocalDateTime updatedAt;
}
