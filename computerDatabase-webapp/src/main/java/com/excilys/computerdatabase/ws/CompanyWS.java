package com.excilys.computerdatabase.ws;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.computerdatabase.dtos.CompanyDTO;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Page;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.mappers.impl.CompanyMapper;
import com.excilys.computerdatabase.services.interfaces.CompanyService;

@RestController
@RequestMapping(value = "/api/company", produces = MediaType.APPLICATION_JSON_VALUE)
//@Secured("ROLE_USER")
public class CompanyWS {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CompanyWS.class);

    @GetMapping
    public ResponseEntity<?> get(@Valid @ModelAttribute Page<?> page) {
        LOGGER.info("get(page: " + page + ")");
        return ResponseEntity.ok(companyService.getPage(page));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") long id) {
        LOGGER.info("get(id: " + id + ")");
        try {
            return ResponseEntity.ok().headers(addAccessControllAllowOrigin())
                    .body(companyMapper.dto(companyService.getById(id)));
        } catch (NotFoundException e) {
            LOGGER.error("Computer " + id + " Not Found");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id) {
        LOGGER.info("delete(id: " + id + ")");
        try {
            companyService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/company")
    public ResponseEntity<?> getCompanies(@PathVariable(value = "id") long id) {
        LOGGER.info("getCompany(id: " + id + ")");
        try {
            return ResponseEntity.ok(companyService.getComputers(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
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
