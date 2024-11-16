package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login.xhtml", "/resources/**").permitAll() // Allow access to log and resources
                        .anyRequest().authenticated() // All other pages require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login.xhtml") // Custom login page
                        .permitAll() // Allow access to the login page for everyone
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login.xhtml?logout") // Redirect to login page on logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.inMemoryAuthentication()
                .withUser("user") // Username
                .password("{noop}password") // Password with {noop} prefix (no encryption)
                .roles("USER") // User role
                .and()
                .withUser("admin") // Admin user for testing
                .password("{noop}admin") // Admin password
                .roles("ADMIN"); // Admin role

        return auth.build();
    }
}
