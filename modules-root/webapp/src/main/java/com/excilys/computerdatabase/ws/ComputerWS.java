package com.excilys.computerdatabase.ws;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.excilys.computerdatabase.dtos.ComputerDTO;
import com.excilys.computerdatabase.dtos.Page;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.ComputerMapper;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.services.interfaces.CompanyService;
import com.excilys.computerdatabase.services.interfaces.ComputerService;
import com.excilys.computerdatabase.validators.ComputerValidator;

@RestController
@RequestMapping(value = "/api/computer", produces = MediaType.APPLICATION_JSON_VALUE)
//@Secured("ROLE_USER")
public class ComputerWS {

    @Autowired
    private ComputerService computerService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ComputerMapper computerMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerWS.class);

    @GetMapping
    public ResponseEntity<?> get(@Valid @ModelAttribute Page<ComputerDTO> page) {
        LOGGER.info("get(page: " + page + ")");
        return ResponseEntity.ok(computerService.getPage(page).getObjects());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") long id) {
        LOGGER.info("get(id: " + id + ")");
        try {
            return ResponseEntity.ok(computerService.getById(id));
        } catch (NotFoundException e) {
            LOGGER.error("Computer " + id + " Not Found");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> post(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO) {
        LOGGER.info("post(computerDTO: " + computerDTO + ")");
        try {
            computerDTO.setCompany(companyService.getById(computerDTO.getCompany().getId()));
            Computer computer = computerMapper.bean(computerDTO);
            ComputerValidator.validate(computer);
            computerDTO = computerService.save(computer);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(computerDTO.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NameEmptyException | IntroducedAfterDiscontinuedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> put(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO) {
        LOGGER.info("update(computerDTO: " + computerDTO + ")");
        try {
            computerDTO.setCompany(companyService.getById(computerDTO.getCompany().getId()));
            Computer computer = computerMapper.bean(computerDTO);
            ComputerValidator.validate(computer);
            return ResponseEntity.ok(computerService.update(computer));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NameEmptyException | IntroducedAfterDiscontinuedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        LOGGER.info("delete(id: " + id + ")");
        try {
            computerService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/company")
    public ResponseEntity<?> getCompanies(@PathVariable(value = "id") long id) {
        LOGGER.info("getCompany(id: " + id + ")");
        try {
            return ResponseEntity.ok(computerService.getCompany(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
