package com.excilys.computerdatabase.services.interfaces;

import java.util.Set;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.UserRole;
import com.excilys.computerdatabase.services.interfaces.template.CrudService;

public interface RoleService extends CrudService<UserRole, UserRole>{
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
