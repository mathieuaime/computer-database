package com.excilys.computerdatabase.daos.interfaces;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

public interface UserDAO {
    User findByUserName(String username) throws UserNotFoundException;
}
