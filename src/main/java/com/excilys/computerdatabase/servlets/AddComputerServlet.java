package com.excilys.computerdatabase.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.services.CompanyServiceImpl;
import com.excilys.computerdatabase.services.ComputerServiceImpl;

public class AddComputerServlet extends HttpServlet {

    private static final long serialVersionUID = -82009216108348436L;

    private ComputerServiceImpl computerService;

    private CompanyServiceImpl companyService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Config.getProperties().getProperty("date_format"));

    public AddComputerServlet() {
        super();
        computerService = new ComputerServiceImpl();
        companyService = new CompanyServiceImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/views/addComputer.jsp");
        
        List<CompanyDTO> companies = companyService.get();
        
        request.setAttribute("companies", companies);

        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        ComputerDTO computerDTO = new ComputerDTO();
        computerDTO.setId(Long.parseLong(request.getParameter("id")));
        computerDTO.setName(request.getParameter("name"));

        
        LocalDate introduced = LocalDate.parse(request.getParameter("introduced"), DATE_FORMATTER);
        LocalDate discontinued = LocalDate.parse(request.getParameter("discontinued"), DATE_FORMATTER);
        
        computerDTO.setIntroduced(introduced);
        computerDTO.setDiscontinued(discontinued);
        
        computerDTO.setCompanyId(Long.parseLong(request.getParameter("companyId")));
        
        computerService.add(computerDTO);
    }

}
