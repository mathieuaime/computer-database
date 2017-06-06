package com.excilys.computerdatabase.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.impl.CompanyMapper;
import com.excilys.computerdatabase.mappers.impl.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

@Controller
@RequestMapping("/addComputer")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class AddComputerController {
    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ComputerMapper computerMapper;
    
    @Autowired
    private CompanyMapper companyMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AddComputerController.class);

    /**
     * GET addComputer.
     * @param model model
     * @return redirection
     */
    @GetMapping
    @Secured("ROLE_ADMIN")
    public String get(ModelMap model) {
        LOGGER.info("get()");

        model.addAttribute("user", CommonController.getUsername());
        model.addAttribute("companies", companyService.getPage().getObjects());
        model.addAttribute("computerDTO", new ComputerDTO());
        return "addComputer";
    }

    /**
     * POST addComputer.
     * @param computerDTO the computer to add
     * @param result binding result
     * @param model model
     * @return redirection
     */
    @PostMapping
    @Secured("ROLE_ADMIN")
    public String post(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO, BindingResult result,
            ModelMap model) {
        LOGGER.info("post(computerDTO : " + computerDTO + ")");

        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getPage().getObjects());
            model.addAttribute(result);
            return "addComputer";
        }
        try {
            CompanyDTO companyDTO = companyMapper.dto(companyService.getById(computerDTO.getCompany().getId()));
            computerDTO.setCompany(companyDTO);

            Computer computer = computerMapper.bean(computerDTO);
            computerService.save(computer);
            return "redirect:dashboard";
        } catch (NotFoundException e) {
            if (e instanceof CompanyNotFoundException) {
                model.addAttribute("error", "La company n'existe pas");
            }
            return get(model);
        }
    }
}
