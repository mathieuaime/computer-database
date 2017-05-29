package com.excilys.computerdatabase.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.UserDAO;
import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;
import com.excilys.computerdatabase.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = false)
    public User save(com.excilys.computerdatabase.models.User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }
    
    @Override
    @Transactional(readOnly = false)
    public User update(com.excilys.computerdatabase.models.User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.update(user);
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        return userDao.findByUserName(username);
    }
}
