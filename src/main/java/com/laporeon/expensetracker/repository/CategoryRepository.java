package com.laporeon.expensetracker.repository;

import com.laporeon.expensetracker.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    boolean existsByName(String name);
    Optional<Category> findByName(String name);
    
}
