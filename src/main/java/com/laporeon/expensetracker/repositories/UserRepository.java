package com.laporeon.expensetracker.repositories;

import com.laporeon.expensetracker.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

   @Query(value = "{ $or: [ { 'username': { $eq: ?0 } }, { 'email': { $eq: ?0 } } ] }")
   Optional<UserDetails> findByLogin(String login);

   boolean existsByEmail(String email);
   boolean existsByUsername(String username);
}
