package com.excilys.computerdatabase.daos.interfaces;

import java.util.Set;

import com.excilys.computerdatabase.daos.interfaces.template.CrudDAO;
import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.UserRole;

public interface UserRoleDAO extends CrudDAO<UserRole>{
    /**
     * Create several roles for a user.
     * @param userRole the roles
     * @throws UserNotFoundException when the user doesn't exist
     */
    void save(Set<UserRole> userRole) throws UserNotFoundException;

    /**
     * Update roles for a user.
     * @param userRole the roles
     * @throws UserNotFoundException when the user doesn't exist
     */
    void update(Set<UserRole> userRole) throws UserNotFoundException;

}
