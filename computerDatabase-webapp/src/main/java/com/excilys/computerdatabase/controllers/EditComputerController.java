package com.excilys.computerdatabase.controllers;

import java.time.format.DateTimeFormatter;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.impl.CompanyMapper;
import com.excilys.computerdatabase.mappers.impl.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;
import com.excilys.computerdatabase.services.interfaces.ComputerService;

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

    @Autowired
    private DateTimeFormatter formatter;

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
        model.addAttribute("isAdmin", SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("companies", companyService.getPage().getObjects());
        model.addAttribute("dateFormat", formatter);

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
            computerService.update(computer);

            LOGGER.debug("update");
            return "redirect:dashboard";
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
