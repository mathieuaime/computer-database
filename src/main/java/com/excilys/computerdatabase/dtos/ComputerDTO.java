package com.excilys.computerdatabase.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class ComputerDTO {

    private long id;

    @NotNull(message = "{label.error.nameEmpty}")
    private String name;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate introduced;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate discontinued;

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

    public LocalDate getIntroduced() {
        return introduced;
    }

    public void setIntroduced(LocalDate introduced) {
        this.introduced = introduced;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(LocalDate discontinued) {
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
