package com.haulmont.testtask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.text.html.parser.Entity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceConfig {

    private static Connection instance;

    private static EntityManagerFactory entityManagerFactory;

    public static synchronized Connection getInstance() {
        if (instance == null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                instance = DriverManager.getConnection("jdbc:hsqldb:file:/home/owlet/file", "SA", "1234");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        EntityManager manager = entityManagerFactory.createEntityManager();
        return instance;
    }
}
