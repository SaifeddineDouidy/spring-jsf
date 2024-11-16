package com.example.beans;

import com.example.model.User;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@RequestScoped
public class UserBean implements Serializable {

    @Inject
    private UserService userService;

    private List<User> users;

    @PostConstruct
    public void init() {
//       users = userService.getAllUsers();
    }

    public List<User> getUsers() {
        return users;
    }
}

