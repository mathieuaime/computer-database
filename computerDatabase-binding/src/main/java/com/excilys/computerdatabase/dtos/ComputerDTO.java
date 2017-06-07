package com.excilys.computerdatabase.dtos;

import javax.validation.constraints.NotNull;

import com.excilys.computerdatabase.validators.VerificationComputerDTO;

@VerificationComputerDTO
public class ComputerDTO {

    private long id;

    @NotNull(message = "{label.error.nameEmpty}")
    private String name;

    private String introduced;

    private String discontinued;

    private CompanyDTO company;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduced() {
        return introduced;
    }

    public void setIntroduced(String introduced) {
        this.introduced = introduced;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return id + "/" + name + "/" + introduced + "/" + discontinued + "/" + company;
    }
}
