package com.example.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@ApplicationScoped
public class CDIConfiguration {

    @Produces
    public ApplicationContext applicationContext() {
        return new AnnotationConfigApplicationContext(SpringContextConfig.class, SecurityConfig.class);
    }
}

