package com.excilys.computerdatabase.daos.impl;

import java.util.List;
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
public class HQLUserRoleDAOImpl implements UserRoleDAO {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HQLUserRoleDAOImpl.class);

    @Override
    public UserRole save(UserRole role) throws UserNotFoundException {
        LOGGER.info("save(role : " + role + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.save(role);
            return role;
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

    @Override
    public UserRole update(UserRole role) throws UserNotFoundException {
        LOGGER.info("update(role : " + role + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.update(role);
            return role;
        } catch (ConstraintViolationException e) {
            throw new UserNotFoundException("User " + role.getUser().getUsername() + " not Found");
        }
    }

    @Override
    public void update(Set<UserRole> userRole) throws UserNotFoundException {
        for (UserRole role : userRole) {
            update(role);
        }
    }

    @Override
    public UserRole getById(long id) throws UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserRole> getByName(String name) {
        throw new UnsupportedOperationException();
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
