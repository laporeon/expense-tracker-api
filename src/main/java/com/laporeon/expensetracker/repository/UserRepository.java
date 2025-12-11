package com.laporeon.expensetracker.repository;

import com.laporeon.expensetracker.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

   boolean existsByEmail(String email);
   boolean existsByUsername(String username);
}
