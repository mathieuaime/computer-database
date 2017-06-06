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
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.CompanyMapper;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;
import com.excilys.computerdatabase.services.interfaces.ComputerService;
import com.excilys.computerdatabase.validators.ComputerValidator;

@Controller
@RequestMapping("/editComputer")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class EditComputerController {
    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ComputerMapper computerMapper;
    
    @Autowired
    private CompanyMapper companyMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EditComputerController.class);

    /**
     * GET editComputer.
     * @param model model
     * @param id the id of the computer
     * @return redirection
     */
    @GetMapping
    @Secured("ROLE_ADMIN")
    public String get(ModelMap model, @RequestParam(value = "id", defaultValue = "0") long id) {
        LOGGER.info("get(id : " + id + ")");
        model.addAttribute("user", CommonController.getUsername());
        model.addAttribute("companies", companyService.getPage().getObjects());
        model.addAttribute("dateFormat", Config.getProperties().getProperty("date_format"));

        ComputerDTO computerDTO = null;

        try {
            computerDTO = computerMapper.dto(computerService.getById(id));
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                LOGGER.error("ComputerNotFound");
                model.addAttribute("error", "Computer inconnu");
            } else if (e instanceof CompanyNotFoundException) {
                LOGGER.error("ComputerNotFound");
                model.addAttribute("error", "Company inconnu");
            }
        }

        model.addAttribute("computerDTO", computerDTO);
        model.addAttribute("companies", companyService.getPage().getObjects());

        return "editComputer";
    }

    /**
     * POST editComputer.
     * @param computerDTO the computer to edit
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
            return "500";
        }

        try {
            CompanyDTO companyDTO = companyMapper.dto(companyService.getById(computerDTO.getCompany().getId()));
            computerDTO.setCompany(companyDTO);

            Computer computer = computerMapper.bean(computerDTO);
            ComputerValidator.validate(computer);
            computerService.update(computer);

            LOGGER.debug("update");
            return "redirect:dashboard";
        } catch (IntroducedAfterDiscontinuedException e) {
            LOGGER.error("IntroducedAfterDiscontinued");
            model.addAttribute("error", "La date d'ajout doit être antérieure à la date de retrait");
            return get(model, computerDTO.getId());
        } catch (NameEmptyException e) {
            LOGGER.error("NameEmpty");
            model.addAttribute("error", "{label.error.nameEmpty}");
            return get(model, computerDTO.getId());
        } catch (NotFoundException e) {
            if (e instanceof ComputerNotFoundException) {
                LOGGER.error("ComputerNotFound");
                model.addAttribute("error", "Le computer n'existe pas");
                return get(model, computerDTO.getId());
            } else if (e instanceof CompanyNotFoundException) {
                LOGGER.error("CompanyNotFound");
                model.addAttribute("error", "La company n'existe pas");
                return get(model, computerDTO.getId());
            } else {
                return "500";
            }
        }
    }
}
