package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login.xhtml", "/resources/**", "/test/invalidateSession").permitAll() // Allow access to login and resources
                        .anyRequest().authenticated() // Require authentication for all other pages
                )
                .formLogin(form -> form
                        .loginPage("/login.xhtml") // Custom login page
                        .permitAll() // Allow access to login page for everyone
                        .defaultSuccessUrl("/home.xhtml", true) // Redirect to home page after successful login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Default logout URL
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)) // Return HTTP 200 on logout
                        .logoutSuccessUrl("/login.xhtml?logout") // Redirect to login page after logout
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/error/403.xhtml")) // Custom Access Denied handler
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login.xhtml")) // Entry point for unauthorized requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session only when required
                        .invalidSessionUrl("/login.xhtml?invalid-session") // Redirect to login page if session is invalid
                        .maximumSessions(1)  // Limit the user to one session
                        .expiredUrl("/login.xhtml?session-expired")  // Redirect to login page if the session expires
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        return auth.build();
    }
}
