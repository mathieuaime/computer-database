package com.excilys.computerdatabase.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 6465944299510271447L;

    /**
     * GET Dashboard.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/views/dashboard.jsp");

        ComputerServiceImpl computerService = new ComputerServiceImpl();

        //TODO valeurs par défaut dans config.properties
        int page = (request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1);
        int length = (request.getParameter("length") != null ? Integer.parseInt(request.getParameter("length")) : 10);

        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        
        int computerCount = computerService.count(search);

        double nbPage = Math.ceil((float) computerCount / length);

        int[] lengths = new int[]{10, 50, 100};

        request.setAttribute("computerPage", computerService.getPage(page, length, search, sort, order));
        request.setAttribute("computerCount", computerCount);
        request.setAttribute("page", page);
        request.setAttribute("search", search);
        request.setAttribute("sort", sort);
        request.setAttribute("order", order);
        request.setAttribute("length", length);
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("lengths", lengths);

        view.forward(request, response);

    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] listComputersToDelete = request.getParameterValues("cb");
    }

}
