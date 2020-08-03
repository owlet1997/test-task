package com.haulmont.testtask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceConfig {

    private static Connection instance;

    public static synchronized Connection getInstance() {
        if (instance == null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                instance = DriverManager.getConnection("jdbc:hsqldb:file:db", "sa", "");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
