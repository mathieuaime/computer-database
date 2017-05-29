package com.excilys.computerdatabase.daos.interfaces;

import java.util.Set;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.UserRole;

public interface UserRoleDAO {
    /**
     * Create a role for a user.
     * @param role the role
     * @throws UserNotFoundException when the user doesn't exist
     */
    void save(UserRole role) throws UserNotFoundException;

    /**
     * Create several roles for a user.
     * @param userRole the roles
     * @throws UserNotFoundException when the user doesn't exist
     */
    void save(Set<UserRole> userRole) throws UserNotFoundException;

    /**
     * Update a role for a user.
     * @param role the role
     * @throws UserNotFoundException when the user doesn't exist
     */
    void update(UserRole role) throws UserNotFoundException;

    /**
     * Update roles for a user.
     * @param userRole the roles
     * @throws UserNotFoundException when the user doesn't exist
     */
    void update(Set<UserRole> userRole) throws UserNotFoundException;

}
