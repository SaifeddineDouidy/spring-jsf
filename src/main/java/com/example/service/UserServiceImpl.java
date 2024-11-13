package com.example.service;

import com.example.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getAllUsers() {
        return List.of(
                new User("saif", "saif@gmail.com")
        );
    }
}