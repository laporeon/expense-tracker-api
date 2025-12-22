package com.laporeon.expensetracker.repositories;

import com.laporeon.expensetracker.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
//
//   @Query(value = "{ $or: [ { 'username': { $eq: ?0 } }, { 'email': { $eq: ?0 } } ] }")
//   Optional<User> findByLogin(String login);

   boolean existsByEmail(String email);
   boolean existsByUsername(String username);
}
