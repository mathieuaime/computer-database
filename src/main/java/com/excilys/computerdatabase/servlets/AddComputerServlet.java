package com.excilys.computerdatabase.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.services.CompanyServiceImpl;
import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class AddComputerServlet extends HttpServlet {

    private static final long serialVersionUID = -82009216108348436L;

    private ComputerServiceImpl computerService;

    private CompanyServiceImpl companyService;

    /**
     * AddComputerServlet default constructor : initialize the daos.
     */
    public AddComputerServlet() {
        super();
        computerService = new ComputerServiceImpl();
        companyService = new CompanyServiceImpl();
    }

    /**
     * GET addComputer.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/views/addComputer.jsp");

        List<CompanyDTO> companies = companyService.get(); // TODO voir si trier les companies

        request.setAttribute("companies", companies);

        view.forward(request, response);

    }

    /**
     * POST addComputer.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComputerDTO computerDTO = new ComputerDTO();

        computerDTO.setName(request.getParameter("name"));
        computerDTO.setIntroduced(request.getParameter("introduced"));
        computerDTO.setDiscontinued(request.getParameter("discontinued"));
        computerDTO.setCompanyId(Long.parseLong(request.getParameter("companyId")));

        try {
            computerService.add(computerDTO);
        } catch (IntroducedAfterDiscontinuedException e) {
            //TODO envoyer erreur au user
        } catch (NameEmptyException e) {
            // TODO envoyer erreur au user
        }

        response.sendRedirect("dashboard");
    }

}
