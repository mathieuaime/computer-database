package com.excilys.computerdatabase.daos.interfaces;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

public interface UserDAO {
    /**
     * Find a user by username.
     * @param username username
     * @return User
     * @throws UserNotFoundException when the user doesn't exist
     */
    User findByUserName(String username) throws UserNotFoundException;

    /**
     * Create a user.
     * @param user user
     * @return User
     */
    User save(User user);
}
