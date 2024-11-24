package com.example.beans;

import com.example.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private UserDetails userDetails;

//    DON'T TOUCH THESE LINE
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    @Inject
    private UserService userService;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void login() {
        try {

            // Check if email or password is empty
            if (email == null || email.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new jakarta.faces.application.FacesMessage("Email is required."));
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new jakarta.faces.application.FacesMessage("Password is required."));
                return;
            }

            // Authenticate the user using UserService
            var userOptional = userService.authenticate(email, password);

            if (userOptional.isPresent()) {
                // If authentication is successful, set up Spring Security context
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, password);

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(
                        (HttpServletRequest) FacesContext.getCurrentInstance()
                                .getExternalContext()
                                .getRequest()
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Store the user details in the session
                this.userDetails = userService.getUserDetails(email);

                // Redirect to the dashboard page after successful login
                FacesContext.getCurrentInstance().getExternalContext().redirect("sessions.xhtml");
            } else {
                // Handle invalid credentials (e.g., show error message)
                FacesContext.getCurrentInstance().addMessage(null,
                        new jakarta.faces.application.FacesMessage("Invalid username or password"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        SecurityContextHolder.clearContext();
        this.userDetails = null;
        try {
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect("login.xhtml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
