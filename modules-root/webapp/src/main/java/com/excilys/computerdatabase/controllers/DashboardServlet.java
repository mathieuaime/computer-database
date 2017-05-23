package com.excilys.computerdatabase.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Controller
public class DashboardServlet  {

    @Autowired
    private ComputerService computerService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DashboardServlet.class);

    private static final String PAGE_DEFAULT = "1";
    private static final String PAGE_SIZE_DEFAULT = "10";

    /**
     * GET Dashboard.
     * @param request request
     * @param response response
     */
    @GetMapping(value = "/dashboard")
    public String get(ModelMap model, @Valid @ModelAttribute Page<ComputerDTO> page) {
        LOGGER.info("get");
        
        LOGGER.info(SecurityContextHolder.getContext().getAuthentication().getName());
        
        int computerCount = computerService.count(page.getSearch());

        model.addAttribute("computerPage", computerService.getPage(page));
        model.addAttribute("computerCount", computerCount);
        model.addAttribute("page", page.getPage());
        model.addAttribute("search", page.getSearch());
        model.addAttribute("order", page.getOrder());
        model.addAttribute("column", page.getColumn());
        model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("nbPage", Math.ceil((float) computerCount / page.getPageSize()));
        model.addAttribute("pageSizes", new int[] { 10, 50, 100 });

        return "dashboard";
    }

    /**
     * POST Dashboard.
     * @param request request
     * @param response response
     */
    @PostMapping(value = "/dashboard")
    public String post(ModelMap model, @RequestParam(value = "selection") String selection) {
        LOGGER.info("post(selection : " + selection + ")");
        String[] listComputersToDelete = selection.split(",");

        List<Long> ids = new ArrayList<Long>(listComputersToDelete.length);

        for (String s : listComputersToDelete) {
            ids.add(Long.parseLong(s));
        }

        try {
            computerService.delete(ids);
        } catch (ComputerNotFoundException e) {
            model.addAttribute("error", "Computer inconnu");
        }

        return get(model, new Page<ComputerDTO>(Integer.parseInt(PAGE_DEFAULT), Integer.parseInt(PAGE_SIZE_DEFAULT)));
    }

    @GetMapping(value = "/login")
    public String login(ModelMap model, @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }

        return "login";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
    
    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

    @GetMapping(value = "/403")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "403";
    }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername() + ((UserDetails) principal).getAuthorities().toString();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
