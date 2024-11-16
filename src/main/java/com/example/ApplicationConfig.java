package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ApplicationScoped
@FacesConfig
@EnableJpaRepositories(basePackages = "com.example.repository")
@ComponentScan(basePackages = "com.example")
public class ApplicationConfig {
    // This class is used to activate Faces with CDI.
}
