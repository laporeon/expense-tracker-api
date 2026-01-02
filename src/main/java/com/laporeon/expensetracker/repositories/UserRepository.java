package com.laporeon.expensetracker.repositories;

import com.laporeon.expensetracker.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

   Optional<User> findByEmail(String email);

   Optional<User> findByIdAndActiveTrue(String id);

   Optional<User> findByIdAndActiveFalse(String id);

   boolean existsByEmail(String email);

}
