package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.custom.CustomUserDetails;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class UserService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
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

    /**
     * Get the user details by email.
     *
     * @param email The email of the user.
     * @return the UserDetails object for the given email.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public UserDetails getUserDetails(String email) {
        // Retrieve the user from the repository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        // Map the user entity to a CustomUserDetails object
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword());
                // IDK we can use roles in the future
                //user.getRoles());
    }
}