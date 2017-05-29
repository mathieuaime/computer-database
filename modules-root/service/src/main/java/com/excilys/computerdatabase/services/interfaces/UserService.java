package com.excilys.computerdatabase.services.interfaces;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

public interface UserService {
    /**
     * Find a user by username.
     * @param username username
     * @return User
     * @throws UserNotFoundException when the user doesn't exist
     */
    User findByUsername(String username) throws UserNotFoundException;

    /**
     * Create a user.
     * @param user user
     * @return User
     */
    User save(User user);
    

    /**
     * Update a user.
     * @param user user
     * @return User
     */
    User update(User user);
}
