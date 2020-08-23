package com.haulmont.testtask.config;

import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.entities.Order;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;

public class DataSourceConfig {
    private static final SessionFactory sessionFactory;

    static {
        AnnotationConfiguration cnf = new AnnotationConfiguration();
        cnf.setProperty(Environment.DRIVER, "org.hsqldb.jdbcDriver");
        cnf.setProperty(Environment.URL, "jdbc:hsqldb:mem:Workout");
        cnf.setProperty(Environment.USER, "sa");
        cnf.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
        cnf.setProperty(Environment.SHOW_SQL, "true");
        cnf.addAnnotatedClass(Client.class);
        cnf.addAnnotatedClass(Master.class);
        cnf.addAnnotatedClass(Order.class);
        sessionFactory = cnf.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
