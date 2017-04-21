package com.excilys.computerdatabase.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.CompanyServiceImpl;
import com.excilys.computerdatabase.services.ComputerServiceImpl;
import com.excilys.computerdatabase.validators.ComputerValidator;

public class EditComputerServlet extends HttpServlet {

    private static final long serialVersionUID = -82009216108348436L;

    private ComputerServiceImpl computerService;

    private CompanyServiceImpl companyService;

    /**
     * AddComputerServlet default constructor : initialize the daos.
     */
    public EditComputerServlet() {
        super();
        computerService = new ComputerServiceImpl();
        companyService = new CompanyServiceImpl();
    }

    /**
     * GET editComputer.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idComputer = (request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0);
        
        ComputerDTO computerDTO = null;

        try {
            computerDTO = computerService.getById(idComputer);
        } catch (ComputerNotFoundException e) {
            request.setAttribute("error", "Computer inconnu");
        }

        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/views/editComputer.jsp");
        
        request.setAttribute("computer", computerDTO);

        List<CompanyDTO> companies = companyService.get(); // TODO voir si trier les companies

        request.setAttribute("companies", companies);

        view.forward(request, response);

    }

    /**
     * POST editComputer.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComputerDTO computerDTO = new ComputerDTO();
        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId(Long.parseLong(request.getParameter("companyId")));

        computerDTO.setId(Long.parseLong(request.getParameter("id")));
        computerDTO.setName(request.getParameter("name"));
        computerDTO.setIntroduced(request.getParameter("introduced"));
        computerDTO.setDiscontinued(request.getParameter("discontinued"));
        computerDTO.setCompany(companyDTO);

        Computer computer = ComputerMapper.createBean(computerDTO);

        try {
            ComputerValidator.validate(computer);
            computerService.update(computer);
            response.sendRedirect("dashboard");
        } catch (IntroducedAfterDiscontinuedException e) {
            request.setAttribute("error", "La date d'ajout doit être antérieure à la date de retrait");
            doGet(request, response);
        } catch (NameEmptyException e) {
            request.setAttribute("error", "Le nom doit être spécifié");
            doGet(request, response);
        } catch (ComputerNotFoundException e) {
            request.setAttribute("error", "Le computer n'existe pas");
            doGet(request, response);
        }
    }

}
