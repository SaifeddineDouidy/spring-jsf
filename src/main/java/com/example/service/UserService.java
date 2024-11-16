package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class UserService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Inject
    private UserRepository userRepository;

    /**
     * Authenticate the user using email and raw password.
     *
     * @param email        The user's email.
     * @param rawPassword  The user's raw password entered during login.
     * @return an Optional containing the authenticated user if successful, otherwise empty.
     */
    public Optional<User> authenticate(String email, String rawPassword) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Verify password if user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (rawPassword.equals(user.getPassword())) { // Compare the raw password with the stored password
                return Optional.of(user); // Authentication successful
            }
        }

        return Optional.empty(); // Authentication failed
    }

    /**
     * Find a user by email.
     *
     * @param email The email of the user.
     * @return the user if found, or null if not.
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}