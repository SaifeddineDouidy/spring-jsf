package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceUnit(unitName = "myPersistenceUnit")
    private EntityManagerFactory emf;

    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}