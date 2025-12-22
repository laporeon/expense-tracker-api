package com.laporeon.expensetracker.helpers;

import com.laporeon.expensetracker.exceptions.AlreadyRegisteredException;
import com.laporeon.expensetracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomValidator {

    private final UserRepository userRepository;

    public void ensureUniqueFields(String username, String email) {
        if (username != null && !username.isEmpty() && userRepository.existsByUsername(username)) {
            throw new AlreadyRegisteredException("Username already taken");
        }

        if (email != null && !email.isEmpty() && userRepository.existsByEmail(email)) {
            throw new AlreadyRegisteredException("Email already registered");
        }
    }
}
