package com.laporeon.expensetracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @MongoId
    private String id;

    @Indexed(unique = true, name = "idx_category_name")
    private String name;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;
}
