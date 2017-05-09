package com.excilys.computerdatabase.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.impl.CompanyServiceImpl;
import com.excilys.computerdatabase.services.impl.ComputerServiceImpl;
import com.excilys.computerdatabase.validators.ComputerValidator;

public class EditComputerServlet extends HttpServlet {

    private static final long serialVersionUID = -82009216108348436L;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Config.getProperties().getProperty("date_format"));

    private ComputerServiceImpl computerService = ComputerServiceImpl.INSTANCE;

    private CompanyServiceImpl companyService = CompanyServiceImpl.INSTANCE;

    /**
     * GET editComputer.
     * @param request request
     * @param response response
     * @throws ServletException servelt exception
     * @throws IOException io exception
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("dateFormat", Config.getProperties().getProperty("date_format"));

        int idComputer = (request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0);

        ComputerDTO computerDTO = null;

        try {
            computerDTO = computerService.getById(idComputer);
        } catch (ComputerNotFoundException e) {
            request.setAttribute("error", "Computer inconnu");
        }

        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/views/editComputer.jsp");

        request.setAttribute("computer", computerDTO);

        //long startTime = System.currentTimeMillis();
        request.setAttribute("companies", companyService.get());
        //long stopTime = System.currentTimeMillis();

        //LOGGER.debug((stopTime - startTime) + " ms");

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

        String introduced = request.getParameter("introduced");
        String discontinued = request.getParameter("discontinued");

        companyDTO.setId(Long.parseLong(request.getParameter("companyId")));

        computerDTO.setId(Long.parseLong(request.getParameter("id")));
        computerDTO.setName(request.getParameter("name"));
        computerDTO.setIntroduced(!introduced.equals("") ? LocalDate.parse(introduced, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DATE_FORMATTER) : "");
        computerDTO.setDiscontinued(!discontinued.equals("") ? LocalDate.parse(discontinued, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DATE_FORMATTER) : "");
        computerDTO.setCompany(companyDTO);

        //LOGGER.debug(computerDTO.toString());

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
