package com.haulmont.testtask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataSourceConfig {

    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("testtask");

    public static EntityManager entityManager;

    public static synchronized EntityManager getInstance() {
        if (entityManager == null) {
                entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
}
