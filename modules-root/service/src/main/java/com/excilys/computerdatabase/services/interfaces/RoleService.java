package com.excilys.computerdatabase.services.interfaces;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;

public interface RoleService {

    void save(User user) throws UserNotFoundException;

}
