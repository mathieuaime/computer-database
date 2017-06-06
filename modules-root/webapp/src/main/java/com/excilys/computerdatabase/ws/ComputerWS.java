package com.excilys.computerdatabase.ws;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;
import com.excilys.computerdatabase.exceptions.NameEmptyException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.impl.CompanyMapper;
import com.excilys.computerdatabase.mappers.impl.ComputerMapper;
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

    @Autowired
    private CompanyMapper companyMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ComputerWS.class);

    @GetMapping
    public ResponseEntity<?> get(@Valid @ModelAttribute Page<ComputerDTO> page) {
        LOGGER.info("get(page: " + page + ")");
        return ResponseEntity.ok().headers(addAccessControllAllowOrigin()).body(computerService.getPage(page));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") long id) {
        LOGGER.info("get(id: " + id + ")");
        try {
            return ResponseEntity.ok().headers(addAccessControllAllowOrigin()).body(computerService.getById(id));
        } catch (NotFoundException e) {
            LOGGER.error("Computer " + id + " Not Found");
            return ResponseEntity.notFound().headers(addAccessControllAllowOrigin()).build();
        }
    }

    @PostMapping
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> post(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO) {
        LOGGER.info("post(computerDTO: " + computerDTO + ")");
        try {
            computerDTO.setCompany(companyMapper.dto(companyService.getById(computerDTO.getCompany().getId())));
            Computer computer = computerMapper.bean(computerDTO);
            ComputerValidator.validate(computer);
            computerDTO = computerMapper.dto(computerService.save(computer));

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(computerDTO.getId()).toUri();

            return ResponseEntity.created(location).headers(addAccessControllAllowOrigin()).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).headers(addAccessControllAllowOrigin()).build();
        } catch (NameEmptyException | IntroducedAfterDiscontinuedException e) {
            return ResponseEntity.badRequest().headers(addAccessControllAllowOrigin()).build();
        }
    }

    @PutMapping
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> put(@Valid @ModelAttribute("computerDTO") ComputerDTO computerDTO) {
        LOGGER.info("update(computerDTO: " + computerDTO + ")");
        try {
            computerDTO.setCompany(companyMapper.dto(companyService.getById(computerDTO.getCompany().getId())));
            Computer computer = computerMapper.bean(computerDTO);
            ComputerValidator.validate(computer);
            return ResponseEntity.ok(computerService.update(computer));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).headers(addAccessControllAllowOrigin()).build();
        } catch (NameEmptyException | IntroducedAfterDiscontinuedException e) {
            return ResponseEntity.badRequest().headers(addAccessControllAllowOrigin()).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        LOGGER.info("delete(id: " + id + ")");
        try {
            computerService.delete(id);
            return ResponseEntity.noContent().headers(addAccessControllAllowOrigin()).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().headers(addAccessControllAllowOrigin()).build();
        }
    }

    @GetMapping(value = "/{id}/company")
    public ResponseEntity<?> getCompanies(@PathVariable(value = "id") long id) {
        LOGGER.info("getCompany(id: " + id + ")");
        try {
            return ResponseEntity.ok().headers(addAccessControllAllowOrigin()).body(computerService.getCompany(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().headers(addAccessControllAllowOrigin()).build();
        }
    }
    
    private HttpHeaders addAccessControllAllowOrigin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        headers.add("Access-Control-Allow-Origin", "*");
        return headers;
    }
}
