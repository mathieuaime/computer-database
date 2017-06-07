package com.excilys.computerdatabase.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.models.User;

@Controller
public class CommonController {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CommonController.class);

    /**
     * GET login.
     * @param model model
     * @param error error
     * @param logout logout
     * @return redirection
     */
    @GetMapping(value = "/login")
    public String login(ModelMap model, @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        LOGGER.info("get");
        if (error != null) {
            switch (error) {
            case "login":
                model.addAttribute("error", "Invalid username and password!");
                break;
            case "register":
                model.addAttribute("error", "Invalid username and password!");
                break;
            }
        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }

        model.addAttribute("User", new User());

        return "login";
    }

    /**
     * GET logout.
     * @param request request
     * @param response response
     * @return redirection
     */
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    /**
     * GET about.
     * @return redirection
     */
    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

    /**
     * GET 403.
     * @param model model
     * @return redirection
     */
    @GetMapping(value = "/403")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("isAdmin", SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("user", getUsername());
        return "403";
    }

    /**
     * GET 404.
     * @param model model
     * @return redirection
     */
    @GetMapping(value = "/404")
    public String notFoundPage(ModelMap model) {
        model.addAttribute("isAdmin", SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("user", getUsername());
        return "404";
    }

    /**
     * GET 500.
     * @param model model
     * @return redirection
     */
    @GetMapping(value = "/500")
    public String errorPage(ModelMap model) {
        model.addAttribute("isAdmin", SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("user", getUsername());
        return "500";
    }

    /**
     * Get the username of the user in session.
     * @return username
     */
    static String getUsername() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}