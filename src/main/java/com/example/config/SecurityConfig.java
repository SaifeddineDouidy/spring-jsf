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
                        .requestMatchers("/login.xhtml", "/resources/**").permitAll() // Allow access to login and resources
                        .anyRequest().authenticated() // Require authentication for all other pages
                )
                .formLogin(form -> form
                        .loginPage("/login.xhtml") // Custom login page
                        .permitAll() // Allow access to login page for everyone
                        .defaultSuccessUrl("/home.xhtml", true) // Redirect to home page after successful login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login.xhtml?logout") // Redirect to login page on logout
                        .permitAll()
                );

        return http.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);

        return auth.build();
    }


}
