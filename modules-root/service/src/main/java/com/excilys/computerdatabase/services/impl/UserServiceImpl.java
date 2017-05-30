package com.excilys.computerdatabase.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.daos.interfaces.UserDAO;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.models.User;
import com.excilys.computerdatabase.services.interfaces.UserService;

@Service
@Transactional(readOnly = false, rollbackFor = NotFoundException.class)
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(com.excilys.computerdatabase.models.User user) throws NotFoundException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public User update(com.excilys.computerdatabase.models.User user) throws NotFoundException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.update(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getByName(String username) {
        return userDao.getByName(username);
    }

    @Override
    @Transactional(readOnly = false)
    public User getById(long id) throws NotFoundException {
        return userDao.getById(id);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        userDao.delete(id);
    }

    @Override
    public void delete(List<Long> ids) throws NotFoundException {
        userDao.delete(ids);
    }
}
