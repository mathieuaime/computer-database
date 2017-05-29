package com.excilys.computerdatabase.daos.impl;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.config.hibernate.HibernateConfig;
import com.excilys.computerdatabase.daos.interfaces.UserDAO;
import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public User findByUserName(String username) throws UserNotFoundException {
        LOGGER.info("findByUserName(username : " + username + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            return session.createQuery("from User where username=:username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("User " + username + " Not Found");
        }
    }

    @Override
    public User save(User user) {
        LOGGER.info("save(user : " + user.getUsername() + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.save(user);
            return user;
        }
    }

    @Override
    public User update(User user) {
        LOGGER.info("update(user : " + user.getUsername() + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.update(user);
            return user;
        }
    }

}
