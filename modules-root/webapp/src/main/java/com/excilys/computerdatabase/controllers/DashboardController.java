package com.excilys.computerdatabase.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Controller
public class DashboardController {

    @Autowired
    private ComputerService computerService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DashboardController.class);

    private static final String PAGE_DEFAULT = "1";
    private static final String PAGE_SIZE_DEFAULT = "10";

    /**
     * GET dashboard.
     * @param model model
     * @param page page
     * @return redirection
     */
    @GetMapping(value = "/dashboard")
    public String get(Locale locale, ModelMap model, @Valid @ModelAttribute Page<ComputerDTO> page) {
        LOGGER.info("get");
        LOGGER.info("Locale : " + locale.getDisplayName());
        LOGGER.info(SecurityContextHolder.getContext().getAuthentication().getName());
        LOGGER.info(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        int computerCount = computerService.count(page.getSearch());

        model.addAttribute("user", CommonController.getPrincipal());
        model.addAttribute("computerPage", computerService.getPage(page));
        model.addAttribute("computerCount", computerCount);
        model.addAttribute("page", page.getPage());
        model.addAttribute("search", page.getSearch());
        model.addAttribute("order", page.getOrder());
        model.addAttribute("column", page.getColumn());
        model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("nbPage", Math.ceil((float) computerCount / page.getPageSize()));
        model.addAttribute("pageSizes", new int[] {10, 50, 100});

        return "dashboard";
    }

    /**
     * POST dashboard.
     * @param model model
     * @param selection computers to delete
     * @return redirection
     */
    @PostMapping(value = "/dashboard")
    public String post(Locale locale, ModelMap model, @RequestParam(value = "selection") String selection) {
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

        return get(locale, model, new Page<ComputerDTO>(Integer.parseInt(PAGE_DEFAULT), Integer.parseInt(PAGE_SIZE_DEFAULT)));
    }
}
