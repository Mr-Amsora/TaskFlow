package com.ammar.taskflow.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectionManager {
    private static ConnectionManager instance;

    private final EntityManagerFactory entityManagerFactory;

    private ConnectionManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("taskflow");
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
