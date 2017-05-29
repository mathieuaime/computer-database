package com.excilys.computerdatabase.services.interfaces;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

public interface RoleService {
    /**
     * Create a role for a user.
     * @param user user
     * @throws UserNotFoundException when the user doesn't exist
     */
    void save(User user) throws UserNotFoundException;
}
