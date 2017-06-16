package com.excilys.computerdatabase.daos.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.config.hibernate.HibernateConfig;
import com.excilys.computerdatabase.daos.interfaces.UserDAO;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.models.User;

@Repository
public class MongoDBUserDAOImpl implements UserDAO {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MongoDBUserDAOImpl.class);

    @Override
    public User update(User user) {
        LOGGER.info("update(user : " + user.getUsername() + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.update(user);
            return user;
        }
    }

    @Override
    public User getById(long id) throws NotFoundException {
        LOGGER.info("getById(id : " + id + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            User user = session.load(User.class, id);
            Hibernate.initialize(user);
            return user;
        }
    }

    @Override
    public List<User> getByName(String name) {
        LOGGER.info("findByUserName(username : " + name + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            return session.createQuery("from User where username=:username", User.class)
                    .setParameter("username", name).getResultList();
        }
    }

    @Override
    public User save(User user) throws NotFoundException {
        LOGGER.info("save(user : " + user.getUsername() + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.save(user);
            return user;
        }
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(List<Long> listId) {
        throw new UnsupportedOperationException();
    }

}
