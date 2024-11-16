package com.example.beans;

import com.example.service.UserService;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@ManagedBean
@RequestScoped
public class LoginBean implements Serializable {

    private String email;
    private String password;

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

    public void login() {
        try {
            // Authenticate the user using UserService
            var userOptional = userService.authenticate(email, password);

            if (userOptional.isPresent()) {
                // If authentication is successful, set up Spring Security context
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, password);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                        (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Redirect to the dashboard page after successful login
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
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
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
