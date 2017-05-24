package com.excilys.computerdatabase.services.interfaces;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

public interface UserService {

    User save(User user);
    User findByUsername(String username) throws UserNotFoundException;

}
