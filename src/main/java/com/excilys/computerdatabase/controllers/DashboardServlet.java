package com.excilys.computerdatabase.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller("dashboard")
@RequestMapping("/dashboard")
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
    @GetMapping
    public String get(ModelMap model, @Valid @ModelAttribute Page<ComputerDTO> page) {
        LOGGER.info("get");
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
    @PostMapping
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
}
