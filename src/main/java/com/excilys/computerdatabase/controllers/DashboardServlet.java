package com.excilys.computerdatabase.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Controller("dashboard")
@RequestMapping("/dashboard")
public class DashboardServlet  {

    @Autowired
    private ComputerService computerService;

    private static final String PAGE_DEFAULT = "1";
    private static final String PAGE_SIZE_DEFAULT = "10";

    /**
     * GET Dashboard.
     * @param request request
     * @param response response
     */
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model,
            @RequestParam(value = "page", required = false, defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE_DEFAULT) int pageSize,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "column", required = false) String column) {

        int computerCount = computerService.count(search);

        model.addAttribute("computerPage", computerService.getPage(page, pageSize, search, column, order));
        model.addAttribute("computerCount", computerCount);
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        model.addAttribute("order", order);
        model.addAttribute("column", column);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("nbPage", Math.ceil((float) computerCount / pageSize));
        model.addAttribute("pageSizes", new int[] { 10, 50, 100 });

        return "dashboard";

    }

    /**
     * POST Dashboard.
     * @param request request
     * @param response response
     */
    @RequestMapping(method = RequestMethod.POST)
    public String post(ModelMap model, @RequestParam(value = "selection") String selection) {
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

        return get(model, Integer.parseInt(PAGE_DEFAULT), Integer.parseInt(PAGE_SIZE_DEFAULT), null, null, null);
    }
}
