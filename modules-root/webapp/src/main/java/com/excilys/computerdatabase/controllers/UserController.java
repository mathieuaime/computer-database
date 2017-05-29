package com.excilys.computerdatabase.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.excilys.computerdatabase.exceptions.UserNotFoundException;
import com.excilys.computerdatabase.models.User;
import com.excilys.computerdatabase.models.UserRole;
import com.excilys.computerdatabase.services.interfaces.RoleService;
import com.excilys.computerdatabase.services.interfaces.UserService;

@Controller
@EnableGlobalMethodSecurity( securedEnabled = true )
public class UserController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    /**
     * POST signup.
     * @param model model
     * @param user user to register
     * @return redirection
     */
    @PostMapping(value = "/signup")
    @Secured("ROLE_ADMIN")
    public String signup(ModelMap model, @Valid @ModelAttribute User user) {
        LOGGER.info("signup(user :" + user.getUsername() + ")");

        try {
            userService.save(user);

            Set<UserRole> sur = new HashSet<UserRole>();
            sur.add(new UserRole(user));

            user.setUserRole(sur);
            roleService.save(user);
            return "redirect:/login";
        } catch (UserNotFoundException e) {
            return "redirect:/login?error=register";
        }
    }
}
