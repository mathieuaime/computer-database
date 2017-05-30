package com.excilys.computerdatabase.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
@EnableGlobalMethodSecurity(securedEnabled = true)
public class AddComputerController {
    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

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
