package com.excilys.computerdatabase.config.hibernate;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;

import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.User;
import com.excilys.computerdatabase.models.UserRole;

public class HibernateConfig {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HibernateConfig.class);

    private static SessionFactory sessionFactory;

    /**
     * Create the Session Factory.
     * @return SessionFactory session factory
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("config/db/hibernate.cfg.xml");
            configuration.addAnnotatedClass(Computer.class);
            configuration.addAnnotatedClass(Company.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(UserRole.class);
            LOGGER.info("Hibernate Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            LOGGER.info("Hibernate serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            LOGGER.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * get a singleton of SessionFactory.
     * @return SessionFactory session factory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    @PostConstruct
    public void initApp() {
        LOGGER.info("Hibernate configuring...");
    }
}
