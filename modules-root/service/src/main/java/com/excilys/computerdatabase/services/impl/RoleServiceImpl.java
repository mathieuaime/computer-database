package com.excilys.computerdatabase.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.daos.interfaces.UserRoleDAO;
import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;
import com.excilys.computerdatabase.services.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    UserRoleDAO roleDAO;

    @Override
    public void save(User user) throws UserNotFoundException {
        roleDAO.save(user.getUserRole());
    }
}
