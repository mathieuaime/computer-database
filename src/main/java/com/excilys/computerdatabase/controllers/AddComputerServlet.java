package com.excilys.computerdatabase.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

@Controller
@RequestMapping("/addComputer")
public class AddComputerServlet {
    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AddComputerServlet.class);

    /**
     * GET addComputer.
     * @param request request
     */
    @GetMapping
    public String get(ModelMap model) {
        LOGGER.info("get()");
        model.addAttribute("companies", companyService.getPage().getObjects());
        model.addAttribute("computerDTO", new ComputerDTO());
        return "addComputer";
    }
    
    /**
     * POST addComputer.
     * @param request request
     * @param response response
     */
    @PostMapping
    public String post(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO, 
            BindingResult result, ModelMap model) {
        LOGGER.info("post(computerDTO : " + computerDTO + ")");

        if (result.hasErrors()) {
            return "500";
        }

        try {
            CompanyDTO companyDTO = companyService.getById(computerDTO.getCompany().getId());
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
