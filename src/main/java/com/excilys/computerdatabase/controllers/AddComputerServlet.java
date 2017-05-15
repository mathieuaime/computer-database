package com.excilys.computerdatabase.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;
import com.excilys.computerdatabase.services.interfaces.ComputerService;
import com.excilys.computerdatabase.validators.ComputerValidator;

public class AddComputerServlet extends HttpServlet {
    private static final long serialVersionUID = -82009216108348436L;
    private ComputerService computerService;
    private CompanyService companyService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern(Config.getProperties().getProperty("date_format"));

    /**
     * Constructor.
     */
    public AddComputerServlet() {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext();
        context.scan("com.excilys.computerdatabase");
        context.refresh();

        computerService = (ComputerService) context.getBean("computerService");
        companyService = (CompanyService) context.getBean("companyService");

        context.close();
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

        request.setAttribute("companies", companyService.getPage().getObjects());

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
        CompanyDTO companyDTO = new CompanyDTO();

        String introduced = request.getParameter("introduced");
        String discontinued = request.getParameter("discontinued");

        companyDTO.setId(Long.parseLong(request.getParameter("companyId")));

        computerDTO.setName(request.getParameter("name"));
        computerDTO.setIntroduced(!introduced.equals("") ? LocalDate.parse(introduced, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DATE_FORMATTER) : "");
        computerDTO.setDiscontinued(!discontinued.equals("") ? LocalDate.parse(discontinued, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DATE_FORMATTER) : "");
        computerDTO.setCompany(companyDTO);

        try {
            Computer computer =  ComputerMapper.createBean(computerDTO);
            ComputerValidator.validate(computer);
            response.sendRedirect("dashboard");
            computerService.add(computer);
        } catch (IntroducedAfterDiscontinuedException e) {
            request.setAttribute("error", "La date d'ajout doit être antérieure à la date de retrait");
            doGet(request, response);
        } catch (NameEmptyException e) {
            request.setAttribute("error", "Le nom doit être spécifié");
            doGet(request, response);
        } catch (CompanyNotFoundException e) {
            request.setAttribute("error", "La company n'existe pas");
            doGet(request, response);
        }
    }
}
