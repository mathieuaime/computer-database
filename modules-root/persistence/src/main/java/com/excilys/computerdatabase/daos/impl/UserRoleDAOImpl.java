package com.excilys.computerdatabase.daos.impl;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.config.hibernate.HibernateConfig;
import com.excilys.computerdatabase.daos.interfaces.UserRoleDAO;
import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.UserRole;

@Repository
public class UserRoleDAOImpl implements UserRoleDAO {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRoleDAOImpl.class);

    @Override
    public void save(UserRole role) throws UserNotFoundException {
        LOGGER.info("save(role : " + role + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.save(role);
        } catch (ConstraintViolationException e) {
            throw new UserNotFoundException("User " + role.getUser().getUsername() + " not Found");
        }
    }

    @Override
    public void save(Set<UserRole> userRole) throws UserNotFoundException {
        for (UserRole role : userRole) {
            save(role);
        }
    }
}
