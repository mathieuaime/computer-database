package com.excilys.computerdatabase.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.daos.interfaces.UserRoleDAO;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.UserRole;
import com.excilys.computerdatabase.services.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    UserRoleDAO roleDAO;

    @Override
    public UserRole save(UserRole user) throws UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserRole update(UserRole user) throws UserNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserRole getById(long id) throws UserNotFoundException {
        try {
            return roleDAO.getById(id);
        } catch (NotFoundException e) {
            throw new UserNotFoundException("User " + id + "Not Found");
        }
    }

    @Override
    public List<UserRole> getByName(String name) {
        return roleDAO.getByName(name);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        roleDAO.delete(id);
    }

    @Override
    public void delete(List<Long> ids) throws NotFoundException {
        roleDAO.delete(ids);
    }

    @Override
    public void save(Set<UserRole> userRole) throws UserNotFoundException {
        roleDAO.save(userRole);
    }

    @Override
    public void update(Set<UserRole> userRole) throws UserNotFoundException {
        roleDAO.update(userRole);
    }
}
