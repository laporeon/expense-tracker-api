package com.laporeon.registrationsystem.repository;

import com.laporeon.registrationsystem.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

   boolean existsByUsernameAndEmail(String username, String email);
   boolean existsByEmail(String email);
   boolean existsByUsername(String username);
}
