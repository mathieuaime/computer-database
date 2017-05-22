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
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;
import com.excilys.computerdatabase.services.interfaces.ComputerService;
import com.excilys.computerdatabase.validators.ComputerValidator;

@Controller
@RequestMapping("/editComputer")
public class EditComputerServlet {
    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EditComputerServlet.class);

    /**
     * GET editComputer.
     * 
     * @param request
     *            request
     * @param response
     *            response
     */
    @GetMapping
    public String get(ModelMap model, @RequestParam(value = "id", defaultValue = "0") long id) {
        LOGGER.info("get(id : " + id + ")");
        model.addAttribute("companies", companyService.getPage().getObjects());
        model.addAttribute("dateFormat", Config.getProperties().getProperty("date_format"));

        ComputerDTO computerDTO = null;

        try {
            computerDTO = computerService.getById(id);
        } catch (ComputerNotFoundException e) {
            LOGGER.error("ComputerNotFound");
            model.addAttribute("error", "Computer inconnu");
        }

        model.addAttribute("computerDTO", computerDTO);
        model.addAttribute("companies", companyService.getPage().getObjects());

        return "editComputer";
    }

    /**
     * POST editComputer.
     * 
     * @param request
     *            request
     * @param response
     *            response
     */
    @PostMapping
    public String post(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO, BindingResult result,
            ModelMap model) {
        LOGGER.info("post(computerDTO : " + computerDTO + ")");

        if (result.hasErrors()) {
            return "500";
        }

        try {
            CompanyDTO companyDTO = companyService.getById(computerDTO.getCompany().getId());
            computerDTO.setCompany(companyDTO);

            Computer computer = ComputerMapper.createBean(computerDTO);
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
        } catch (ComputerNotFoundException e) {
            LOGGER.error("ComputerNotFound");
            model.addAttribute("error", "Le computer n'existe pas");
            return get(model, computerDTO.getId());
        } catch (CompanyNotFoundException e) {
            LOGGER.error("CompanyNotFound");
            model.addAttribute("error", "La company n'existe pas");
            return get(model, computerDTO.getId());
        }
    }
}
