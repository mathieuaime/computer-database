package com.excilys.computerdatabase.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.models.UserRole;
import com.excilys.computerdatabase.services.interfaces.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        LOG.info("loadUserByName(username : " + username + ")");
        List<com.excilys.computerdatabase.models.User> users = userService.getByName(username);
        if (users.isEmpty()) {
            LOG.error("User " + username + " Not Found");
            throw new UsernameNotFoundException("Username " + username + " Not Found");
        } else {
            com.excilys.computerdatabase.models.User user = users.get(0);
            List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRole());
            return buildUserForAuthentication(user, authorities);
        }
    }

    /**
     * Converts com.excilys.computerdatabase.models.User user to
     * org.springframework.security.core.userdetails.User.
     * @param user user
     * @param authorities authorities
     * @return User
     */
    private User buildUserForAuthentication(com.excilys.computerdatabase.models.User user,
            List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
    }

    /**
     * Build user authority.
     * @param userRoles roles
     * @return List of granted authority
     */
    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
        }
        return new ArrayList<GrantedAuthority>(setAuths);
    }
}
