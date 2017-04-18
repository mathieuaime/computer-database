package com.excilys.computerdatabase.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class DashboardServlet extends HttpServlet {

    private int page = 1;
    private int length = 10;

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

        request.setAttribute("computerPage", computerService.getPage(page, length));
        request.setAttribute("computerCount", computerService.count());

        view.forward(request, response);

    }

}
