package com.excilys.computerdatabase.daos.interfaces;

import java.util.Set;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.UserRole;

public interface UserRoleDAO {

    void save(UserRole role) throws UserNotFoundException;
    void save(Set<UserRole> userRole) throws UserNotFoundException;

}
