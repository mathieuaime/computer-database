package com.excilys.computerdatabase.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

@Controller("addComputer")
@RequestMapping("/addComputer")
public class AddComputerServlet {
    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

    /**
     * GET addComputer.
     * @param request request
     */
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model) {
        model.addAttribute("companies", companyService.getPage().getObjects());

        return "addComputer";
    }
    
    /**
     * POST addComputer.
     * @param request request
     * @param response response
     */
    @RequestMapping(method = RequestMethod.POST)
    public String post(ModelMap model,
            @RequestParam("name") String name,
            @RequestParam("introduced") String introduced,
            @RequestParam("discontinued") String discontinued,
            @RequestParam("companyId") long companyId) {

        ComputerDTO computerDTO = new ComputerDTO();

        computerDTO.setName(name);        
        computerDTO.setIntroduced(!introduced.equals("") ? LocalDate.parse(introduced, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null);
        computerDTO.setDiscontinued(!discontinued.equals("") ? LocalDate.parse(discontinued, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null);


        try {
            CompanyDTO companyDTO = companyService.getById(companyId);
            computerDTO.setCompany(companyDTO);

            Computer computer =  ComputerMapper.createBean(computerDTO);
            ComputerValidator.validate(computer);
            computerService.add(computer);

            return "redirect:dashboard";
        } catch (IntroducedAfterDiscontinuedException e) {
            model.addAttribute("error", "La date d'ajout doit être antérieure à la date de retrait");
            return get(model);
        } catch (NameEmptyException e) {
            model.addAttribute("error", "Le nom doit être spécifié");
            return get(model);
        } catch (CompanyNotFoundException e) {
            model.addAttribute("error", "La company n'existe pas");
            return get(model);
        }
    }
}
