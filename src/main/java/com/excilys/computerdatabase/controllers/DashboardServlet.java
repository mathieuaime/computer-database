package com.excilys.computerdatabase.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 6465944299510271447L;

    private ComputerService computerService;

    private static final int PAGE_DEFAULT = Integer.parseInt(Config.getProperties().getProperty("page_default"));
    private static final int PAGE_SIZE_DEFAULT = Integer.parseInt(Config.getProperties().getProperty("page_size_default"));
    
    public DashboardServlet() {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext();
        context.scan("com.excilys.computerdatabase"); 
        context.refresh();
        
        computerService = (ComputerService) context.getBean("computerService");
        
        context.close();
    }

    /**
     * GET Dashboard.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/views/dashboard.jsp");

        int page = (request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : PAGE_DEFAULT);
        int pageSize = (request.getParameter("pageSize") != null ? Integer.parseInt(request.getParameter("pageSize")) : PAGE_SIZE_DEFAULT);

        String search = request.getParameter("search");
        String order = request.getParameter("order");
        String column = request.getParameter("column");

        int computerCount = computerService.count(search);

        request.setAttribute("computerPage", computerService.getPage(page, pageSize, search, column, order));
        request.setAttribute("computerCount", computerCount);
        request.setAttribute("page", page);
        request.setAttribute("search", search);
        request.setAttribute("order", order);
        request.setAttribute("column", column);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("nbPage", Math.ceil((float) computerCount / pageSize));
        request.setAttribute("pageSizes", new int[]{10, 50, 100});

        view.forward(request, response);

    }

    /**
     * POST Dashboard.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] listComputersToDelete = request.getParameter("selection").split(",");

        List<Long> ids = new ArrayList<Long>(listComputersToDelete.length);

        for (String s : listComputersToDelete) {
            ids.add(Long.parseLong(s));
        }

        try {
            computerService.delete(ids);
        } catch (ComputerNotFoundException e) {
            request.setAttribute("error", "Computer inconnu");
        }

        doGet(request, response);
    }

}
